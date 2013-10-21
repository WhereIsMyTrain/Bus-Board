package com.whereismytrain.transitboard;

import com.whereismytrain.transitboard.HomeScreen;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class TravelRoutes extends Activity {


	public final static String ROUTE = "com.whereismytrain.transitboard.ROUTE";
	public static String SINGLE_ROUTE = null;
	String dest;
	String destId = null;
	String origin;
	String originId = null;
	String dateTime;
	String leave;

	JSONObject obj = null;
	
	//ProgressBar routeProgressBar = new ProgressBar(getApplicationContext());
	
	private Handler routeHandler = new Handler() {
		  @Override
		  public void handleMessage(Message msg) {
			   try {
				  bindTravelRoutes(obj);
			  } catch (NullPointerException e) {
				home(new View(getApplicationContext()));
			  } catch (JSONException e) {
				home(new View(getApplicationContext()));
			  }
			  //ProgressBar bar = (ProgressBar)findViewById(R.id.routesProgressBar);
			  //bar.setVisibility(View.INVISIBLE);
			  }
		 };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_travel_routes);		
		Intent intent = getIntent();
		
		origin = intent.getStringExtra(HomeScreen.ORIGIN_ID);
		leave = intent.getStringExtra(HomeScreen.LEAVE_WHEN);
		dateTime = intent.getStringExtra(HomeScreen.DATE_TIME);
		dest = intent.getStringExtra(HomeScreen.E_DESTINATION);
		
		Runnable routesRunnable = new Runnable() {
	        public void run() {     	
	        	synchronized (this) {
	        		
	        		try {
	        			Object[] toLocs;
	        			Object[] fromLocs;
	        			String noRoutesError = "Sorry, no services available between\n" + dest + "and\n" + origin;
	        			
	        			//need to pass the dest and origin through resolveLocationId to get Id
						toLocs = HomeScreen.resolveLocationId(dest);
						fromLocs = HomeScreen.resolveLocationId(origin);
						
						//take the first entry or return to home page if location was not resolved
						View view = new View(getApplicationContext());
						if (toLocs.length > 0 && fromLocs.length > 0) {
							destId = (String) toLocs[0];
							originId = (String) fromLocs[0];
						}
						else 
							home(view);
						
						obj = retrieveTravelRoutes(originId, destId, dateTime, leave);
						
					} catch (JSONException e) {
						Toast.makeText(getApplicationContext(), 
								e.toString(), Toast.LENGTH_LONG).show();
					} catch (NullPointerException e) {
						Toast.makeText(getApplicationContext(), 
								e.toString(), Toast.LENGTH_LONG).show();
					}
	        	}
	        	routeHandler.sendEmptyMessage(0);
	        }
    	};
    	Thread mythread = new Thread(routesRunnable);
    	mythread.start();
    	
    	
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.travel_routes, menu);
		return true;
	}
	
	public JSONObject retrieveTravelRoutes(String originStopId, String destStopId,
			String dateTime, String leave) throws JSONException, NullPointerException{
		
		
		String dest = destStopId.replace(" ", "%20");
		String dest1 = dest.replace(":", "%3A");
		String dest2 = dest1.replace(",", "%2C");

		String orig0 = originStopId.replace(" ", "%20");
		String orig1 = orig0.replace(":", "%3A");
		String orig2 = orig1.replace(",", "%2C");
		//String date = "2013+10+29+18%3A00";

		String url = 
				"http://deco3801-003.uqcloud.net/opia/travel/rest/plan/" + orig2 +
				"/" + dest2 + "?timeMode=" + leave + "&at=" + dateTime + "&" +
				"walkSpeed=1&maximumWalkingDistanceM=500&" +
				"serviceTypes=1&fareTypes=2&api_key=special-key";
		
		//String url = http://deco3801-003.uqcloud.net/opia/travel/rest/plan/AD%3ARobin%20St%2C%20Coalfalls/LM%3ATrain%20Stations%3AMilton%20station?timeMode=3&at=2013+10+29+18%3A00&walkSpeed=1&maximumWalkingDistanceM=500&serviceTypes=1&api_key=special-key
		Jsonp jParser = new Jsonp();
		JSONObject obj = jParser.getJSONFromUrl(url);
		return obj;
		
	}
	public void bindTravelRoutes(JSONObject obj) throws JSONException, NullPointerException{
		JSONObject travelOptions = obj.getJSONObject("TravelOptions");
		JSONArray itin = travelOptions.getJSONArray("Itineraries");
		if (itin.length() == 0)
			home(new View(getApplicationContext()));
		
			for (int i = 0; i < itin.length(); i++) {
				JSONObject it = itin.getJSONObject(i);
				SINGLE_ROUTE = it.toString();
				String duration = it.getString("DurationMins");
				JSONObject fares = it.getJSONObject("Fare");
				String zonesCov = fares.getString("TotalZones");
				String depTimeRaw = it.getString("FirstDepartureTime");
				String depTime = parseDate(depTimeRaw);
				JSONArray legs = it.getJSONArray("Legs");
				JSONObject firstLeg = legs.getJSONObject(0);
				JSONObject firstLegRoute = firstLeg.getJSONObject("Route");
				String code = "walk";
				if (firstLegRoute != null) {
					code = firstLegRoute.getString("Code");
				}
	
				LinearLayout masterLayout = (LinearLayout) findViewById(R.id.travel_routes);
			 	final LinearLayout lineOne = new LinearLayout(this);
			 	ImageView image = new ImageView(getApplicationContext());
			 	image.setImageResource(R.drawable.bus);
			 	TextView info = new TextView(this);
				info.setText("Duration: " + duration + " mins\n" + zonesCov + " zones covered\nDepart: " + 
						depTime + " Take the " + code);
				
				LayoutParams params = new LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				info.setTextSize(15.0f);
				info.setBackgroundColor(Color.LTGRAY);
				params.setMargins(12, 12, 12, 12);
				info.setLayoutParams(params);
				
				
				info.setOnClickListener(new OnClickListener() {
					@Override
		            public void onClick(View viewIn) {
						lineOne.setBackgroundColor(Color.DKGRAY);
						try {
							directions(viewIn, SINGLE_ROUTE);
						} catch (Exception e) {
							Toast.makeText(getApplicationContext(), e.toString()
									, Toast.LENGTH_LONG).show();
							    
						}
		            }
				});
				lineOne.addView(image);
				lineOne.addView(info);
				masterLayout.addView(lineOne);
				
				String instructions ="";
				/* for (int j=0; j < legs.length(); j++) {
					
					LinearLayout lineDir = new LinearLayout(this);
					JSONObject inst = legs.getJSONObject(j);
					TextView dir = new TextView(this);
					dir.setText("Leg " + (j + 1) + ": " + inst.getString("Instruction"));
					lineDir.addView(dir);
					masterLayout.addView(lineDir);
				} */
			}
	}
	
	public void home(View view) {
		Intent i = new Intent(this, HomeScreen.class);
		startActivity(i);
	}
	
	public void directions(View view, String route) throws Exception{
		Intent i = new Intent(this, Directions.class);
		i.putExtra(ROUTE, route);
		startActivity(i);
	}
	
	public String parseDate(String input) {
		//JSON format is "/Date(1381322400000+1000)/"
		Date date = new Date(Long.parseLong(input.substring(6,19)));
		TimeZone timeZone = TimeZone.getTimeZone("GMT" + input.substring(19,24));
		
		SimpleDateFormat formatter = new SimpleDateFormat("EEE HH:mm zzz");
		formatter.setTimeZone(timeZone);
		return formatter.format(date);

	}
	
	public String timeRemaining(String depDate, Long now) {
		Long departure = Long.parseLong(depDate.substring(6,19));
		Long difference = departure - now;
		Date date = new Date(difference);
		
		SimpleDateFormat formatter = new SimpleDateFormat("mm");
		return formatter.format(date);
	}
}
