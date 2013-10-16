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
import android.app.Activity;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TravelRoutes extends Activity {


	public final static String ROUTE = "com.whereismytrain.transitboard.ROUTE";
	public static String SINGLE_ROUTE = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_travel_routes);		
		Intent intent = getIntent();
		
		String originStopId = intent.getStringExtra(HomeScreen.ORIGIN_ID);
		String leave = intent.getStringExtra(HomeScreen.LEAVE_WHEN);
		String dateTime = intent.getStringExtra(HomeScreen.DATE_TIME);
		String dest = intent.getStringExtra(HomeScreen.E_DESTINATION);

		
		
		//need to pass the dest through resolveLocationId to get Id
		String destId = null;
		String originId = null;
		
		Object[] toLocs = null;
		Object[] fromLocs = null;
		try {
			toLocs = HomeScreen.resolveLocationId(dest);
			fromLocs = HomeScreen.resolveLocationId(originStopId);
		} catch (NullPointerException e1) {
			Toast.makeText(getApplicationContext(), 
					e1.toString(), Toast.LENGTH_LONG).show();
		} catch (JSONException e1) {
			Toast.makeText(getApplicationContext(), 
					e1.toString(), Toast.LENGTH_LONG).show();
		}
		
		
		//take the first entry
		View view = new View(getApplicationContext());
		if (toLocs.length > 0 && fromLocs.length > 0) {
			destId = (String) toLocs[0];
			originId = (String) fromLocs[0];
		}
		else 
			home(view);
		
		

		
	    //setContentView(textView);


		/*Toast.makeText(getApplicationContext(), 
				originStopId + "\n" + destId + "\n" + dateTime + "\n" + leave, 
				Toast.LENGTH_LONG).show();*/
		   
		try {
			retrieveTravelRoutes(originStopId, destId, dateTime, leave);
			
		} catch (Exception e) {
			String dest0 = destId.replace(' ', '+');
			String dest1 = dest0.replace(":", "%3A");

			String orig0 = originId.replace(' ', '+');
			String orig1 = orig0.replace(":", "%3A");

			Toast.makeText(getApplicationContext(), 
					e.toString(), Toast.LENGTH_LONG).show();
			/*TextView textView = new TextView(this);
		    textView.setTextSize(40);
		    textView.setText("Received: " + orig1 + "\n" + dest1 + "\n" + dateTime + "\n"
					+ leave);*/
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.travel_routes, menu);
		return true;
	}
	
	public void retrieveTravelRoutes(String originStopId, String destStopId,
			String dateTime, String leave) throws Exception{
		String dest = destStopId.replace(" ", "%20");
		String dest1 = dest.replace(":", "%3A");

		String orig0 = originStopId.replace(" ", "%20");
		String orig1 = orig0.replace(":", "%3A");
		//String date = "2013+09+29+18%3A00";

		String url = 
				"http://deco3801-003.uqcloud.net/opia/travel/rest/plan/" + orig1 +
				"/" + dest1 + "?timeMode=" + leave + "&at=" + dateTime + "&" +
				"walkSpeed=1&maximumWalkingDistanceM=500&" +
				"serviceTypes=1&fareTypes=2&api_key=special-key";
		
		//String url = "http://deco3801-003.uqcloud.net/opia/travel/rest/plan/SI%3A001841/SI%3A002030?timeMode=3&at=2013+09+29+18%3A00&vehicleTypes=2&walkSpeed=1&maximumWalkingDistanceM=500&serviceTypes=1&fareTypes=2&api_key=special-key";
		
		Jsonp jParser = new Jsonp();
		JSONObject obj = jParser.getJSONFromUrl(url);
		

		JSONObject travelOptions = obj.getJSONObject("TravelOptions");
		JSONArray itin = travelOptions.getJSONArray("Itineraries");
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
			 	LinearLayout lineOne = new LinearLayout(this);
				TextView info = new TextView(this);
				info.setText("Duration: " + duration + " mins\n" + zonesCov + " zones covered\nDepart: " + 
						depTime + " Take the " + code);
				info.setOnClickListener(new OnClickListener() {
					@Override
		            public void onClick(View viewIn) {
						try {
							directions(viewIn, SINGLE_ROUTE);
						} catch (Exception e) {
							Toast.makeText(getApplicationContext(), e.toString()
									, Toast.LENGTH_LONG).show();
							    
						}
		            }

				});
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
