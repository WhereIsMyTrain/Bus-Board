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
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ImageView.ScaleType;
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
	
	private Handler routeHandler = new Handler() {
		  @Override
		  public void handleMessage(Message msg) {
			   try {
				  bindTravelRoutes(obj);
			  } catch (JSONException e) {
				  showAlert();
			  } catch (NullPointerException e) {
				  showAlert();
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
		
		try {
		Object[] toLocs;
		Object[] fromLocs;
		
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
			showAlert();
		
		obj = retrieveTravelRoutes(originId, destId, dateTime, leave);
		
		} catch (JSONException e) {
			showAlert();
			
		} catch (NullPointerException e) {
			showAlert();
			
		}
		
		try {
			bindTravelRoutes(obj);
		} catch (NullPointerException e1) {
			showAlert();
		} catch (JSONException e1) {
			showAlert();
		}
		
		Runnable routesRunnable = new Runnable() {
	        public void run() {     	
	        	synchronized (this) {
	        		
	        		try {
	        			Object[] toLocs;
	        			Object[] fromLocs;
	        			
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
						
						Toast.makeText(getApplicationContext(), obj.toString(), Toast.LENGTH_LONG).show();
						
						
					} catch (JSONException e) {
						//showAlert();
						Toast.makeText(getApplicationContext(), originId + "\n" +
								destId + "\n" +
								dateTime + "\n" +
								leave, Toast.LENGTH_LONG).show();
						
					} catch (NullPointerException e) {
						//showAlert();
						Toast.makeText(getApplicationContext(), originId + "\n" +
								destId + "\n" +
								dateTime + "\n" +
								leave, Toast.LENGTH_LONG).show();
					} catch (Exception e) {
						//showAlert();
					}
	        	}
	        	routeHandler.sendEmptyMessage(0);
	        }
    	};
    	Thread mythread = new Thread(routesRunnable);
    	//mythread.start();
 
    	
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.travel_routes, menu);
		return true;
	}
	
	private void showAlert() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
 
			// set title
			alertDialogBuilder.setTitle("Sorry! No Services Available");
 
			// set dialog message
			alertDialogBuilder
				.setMessage("Click 'Back' to start again")
				.setCancelable(false)
				.setPositiveButton("Back",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						// current activity
						TravelRoutes.this.finish();
					}
				  }).setIcon(R.drawable.walk);
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();

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
		//GP%3A-27.4973000007%2C153.011340000000000
		/*String url = "http://deco3801-003.uqcloud.net/opia/travel/rest/plan/" +
				"GP%3A-27.4973000007%2C153.011340000000000/" +
				"LM%3ATrain%20Stations%3AMilton%20station?timeMode=3&at=2013+10+22+18%3A00&" +
				"walkSpeed=1&maximumWalkingDistanceM=500";*/
		
		String url = "http://deco3801-003.uqcloud.net/opia/travel/rest/plan/" + orig2 +
				"/" + dest2 + "?timeMode=" + leave + "&at=" + dateTime + "&" +
				"walkSpeed=1&maximumWalkingDistanceM=1500";
		//Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
		//String url = http://deco3801-003.uqcloud.net/opia/travel/rest/plan/GP%3A-27.4973000007%2C153.011340000000000/LM%3ATrain%20Stations%3AMilton%20station?timeMode=0&at=2013+10+29+18%3A00&walkSpeed=1&maximumWalkingDistanceM=500
		Jsonp jParser = new Jsonp();
		JSONObject obj = jParser.getJSONFromUrl(url);
		return obj;
		
	}
	public void bindTravelRoutes(JSONObject obj) throws JSONException, NullPointerException {
		JSONObject travelOptions = obj.getJSONObject("TravelOptions");
		JSONArray itin = travelOptions.getJSONArray("Itineraries");
		if (itin.length() == 0)
			showAlert();
		
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
				JSONObject secondLeg = null;
				if (legs.length() > 1)
					secondLeg = legs.getJSONObject(1);
				JSONObject firstLegRoute;
				JSONObject secondLegRoute = null;
				String code = "";
				String startingStop = "";
				String startId = firstLeg.getString("ToStopId");
				
				if (!(firstLeg.isNull("Route"))) {
					firstLegRoute = firstLeg.getJSONObject("Route");
					code = "Take the " + firstLegRoute.getString("Code");
				} else if (secondLeg != null && !(secondLeg.isNull("Route"))) {
					secondLegRoute = secondLeg.getJSONObject("Route");
					code = "Take the " + secondLegRoute.getString("Code");
				} 
				
				startingStop = resolveStopId(startId);
					
	
				LinearLayout masterLayout = (LinearLayout) findViewById(R.id.travel_routes);
			 	LinearLayout lineOne = new LinearLayout(this);
			 	ImageView image = new ImageView(getApplicationContext());
			 	ImageView line = new ImageView(getApplicationContext());
			 	line.setImageResource(R.drawable.line2);
			 	image.setImageResource(R.drawable.bus);
			 	
			 	TextView info = new TextView(this);
				info.setText(code + "\n" + "From: " + startingStop + "\n" + "Leaves at: " + 
					depTime +  "\n" + "Duration: " + duration + " mins\n" + zonesCov + " zones covered\n");
				info.setTextSize(15.0f);
				
				LayoutParams textParams = new LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				LayoutParams imageParams = new LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				
				imageParams.setMargins(0, 30, 0, 0);
				textParams.setMargins(12, 12, 12, 12);
				
				info.setLayoutParams(textParams);
				image.setLayoutParams(imageParams);
				line.setScaleType(ScaleType.FIT_XY);
				
				info.setOnClickListener(new OnClickListener() {
					String route = SINGLE_ROUTE;
					@Override
		            public void onClick(View viewIn) {
						try {
							directions(viewIn, route);
						} catch (Exception e) {
							Toast.makeText(getApplicationContext(), e.toString()
									, Toast.LENGTH_LONG).show();
							    
						}
		            }
				});
				lineOne.addView(image);
				lineOne.addView(info);
				masterLayout.addView(lineOne);
				//masterLayout.addView(info);
				masterLayout.addView(line);
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
	
	public void mapScreen(View view) {
		Intent i = new Intent(this, Map.class);
		startActivity(i);
	}
	
	public String parseDate(String input) {
		//JSON format is "/Date(1381322400000+1000)/"
		Date date = new Date(Long.parseLong(input.substring(6,19)));
		//TimeZone timeZone = TimeZone.getTimeZone("GMT" + input.substring(19,24));
		
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		//formatter.setTimeZone(timeZone);
		return formatter.format(date);

	}
	
	public String timeRemaining(String depDate, Long now) {
		Long departure = Long.parseLong(depDate.substring(6,19));
		Long difference = departure - now;
		Date date = new Date(difference);
		
		SimpleDateFormat formatter = new SimpleDateFormat("mm");
		return formatter.format(date);
	}
	
	public String resolveStopId(String id) throws JSONException{
		Jsonp jParser = new Jsonp();
		String url = "http://deco3801-003.uqcloud.net/opia/location/rest/stops?ids=SI%3A" + id;
		JSONObject stopObj = jParser.getJSONFromUrl(url);
		
		JSONObject stopDetail = stopObj.getJSONArray("Stops").getJSONObject(0);
		String description = stopDetail.getString("Description");
		return description;
	}
}
