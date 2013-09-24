package com.whereismytrain.transitboard;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TravelRoutes extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_travel_routes);		
		Intent intent = getIntent();
		
		String originStopId = intent.getStringExtra(HomeScreen.ORIGIN_ID);
		String leave = intent.getStringExtra(HomeScreen.LEAVE_WHEN);
		String dateTime = intent.getStringExtra(HomeScreen.DATE_TIME);
		String dest = intent.getStringExtra(HomeScreen.E_DESTINATION);
		
		TextView error = (TextView)findViewById(R.id.error);
		error.setText(dest);
		Toast.makeText(getApplicationContext(), 
				originStopId + "\n" + dest + "\n" + dateTime + "\n" + leave, 
				Toast.LENGTH_LONG).show();
		   
		try {
			retrieveTravelRoutes(originStopId, dest, dateTime);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), 
                    e.toString(), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.travel_routes, menu);
		return true;
	}
	
	public String[][] retrieveTravelRoutes(String originStopId, String destStopId,
			String dateTime) throws Exception{
		String origin = "001841";
		String dest = "002030";
		String date = "2013+09+29+18%3A00";
		List<String[]> routeList = new ArrayList<String[]>();
		List<String> itineraryList = new ArrayList<String>();
		
		String[][] routes = null;
		String url = 
				"http://deco3801-003.uqcloud.net/opia/travel/rest/plan/SI%3A" + originStopId +
				"/SI%3A" + destStopId + "?timeMode=3&at=" + dateTime + "&" +
				"vehicleTypes=2&walkSpeed=1&maximumWalkingDistanceM=500&" +
				"serviceTypes=1&fareTypes=2&api_key=special-key";
		
		//String url = "http://deco3801-003.uqcloud.net/opia/travel/rest/plan/SI%3A001841/SI%3A002030?timeMode=3&at=2013+09+29+18%3A00&vehicleTypes=2&walkSpeed=1&maximumWalkingDistanceM=500&serviceTypes=1&fareTypes=2&api_key=special-key";
		
		Jsonp jParser = new Jsonp();
		JSONObject obj = jParser.getJSONFromUrl(url);
		

		JSONObject travelOptions = obj.getJSONObject("TravelOptions");
		JSONArray itin = travelOptions.getJSONArray("Itineraries");
			for (int i = 0; i < itin.length(); i++) {
				JSONObject it = itin.getJSONObject(i);
				String duration = it.getString("DurationMins");
				JSONObject fares = it.getJSONObject("Fare");
				String zonesCov = fares.getString("TotalZones");
				String depTime = it.getString("FirstDepartureTime");
				JSONArray legs = it.getJSONArray("Legs");
				
				
				LinearLayout masterLayout = (LinearLayout) findViewById(R.id.travel_routes);
			 	LinearLayout lineOne = new LinearLayout(this);
				TextView info = new TextView(this);
				info.setText("Duration: " + duration + " mins\n" + zonesCov + " zones covered\nDepart: " + 
						depTime);
				lineOne.addView(info);
				masterLayout.addView(lineOne);
				
				String instructions ="";
				for (int j=0; j < legs.length(); j++) {
					
					LinearLayout lineDir = new LinearLayout(this);
					JSONObject inst = legs.getJSONObject(j);
					TextView dir = new TextView(this);
					dir.setText("Leg " + (j + 1) + ": " + inst.getString("Instruction"));
					lineDir.addView(dir);
					masterLayout.addView(lineDir);
				}
				

				
				
				
				
			}
			return routes;
	}
	public void bindRoutes(String[][] routes) {
		String[] itin;
		for (int i = 0; i < routes.length; i ++) {
			itin = null;
			itin = routes[i];
			String duration = itin[0];
			String endTime = itin[1];
			String fare = itin[2];
			String firstDepartureTime = itin[3];
			String lastDepartureTime = itin[4];
			LinearLayout masterLayout = (LinearLayout) findViewById(R.id.travel_routes);
		 	LinearLayout lineOne = new LinearLayout(this);
			TextView info = new TextView(this);
			info.setText(duration + " min " + " " + endTime + " " +
									fare + " " + firstDepartureTime + " " + 
									lastDepartureTime);
			lineOne.addView(info);
			masterLayout.addView(lineOne);
		}
		
	}
}
