package com.whereismytrain.transitboard;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TravelRoutes extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_travel_routes);
		String originStopId = "";
		String destStopId = "";
		String dateTime = "";
		
		try {
			retrieveTravelRoutes(originStopId, destStopId, dateTime);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), 
                    "An error Occurred", Toast.LENGTH_LONG).show();
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
		List<String> list = new ArrayList<String>();
		
		String[][] routes = new String[10][];
		String url = 
				"https://opia.api.translink.com.au/v1/travel/" +
				"rest/plan/SI%3A001841/SI%3A002030?timeMode=1&at" +
				"=09%2F09%2F2013+18%3A00&walkSpeed=1&maximumWalkingDistanceM=" +
				"500&api_key=special-key";
		
		Jsonp jParser = new Jsonp();
		JSONObject obj = jParser.getJSONFromUrl(url);
		
		
			JSONArray travelOptions = obj.getJSONArray("TravelOptions");
			for (int i = 0; i < travelOptions.length(); i++) {
				JSONArray itinerary = travelOptions.getJSONArray(i);
				
				for (int j = 0; j < itinerary.length(); j++) {
					String duration;
					String endTime;
					String fare;
					String firstDepartureTime;
					String lastDepartureTime;
					JSONArray legs;
					
					duration = itinerary.getString(j);
					endTime = itinerary.getString(j);
					fare = itinerary.getString(j);
					firstDepartureTime = itinerary.getString(j);
					lastDepartureTime = itinerary.getString(j);
					legs = itinerary.getJSONArray(j);
					
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
			return routes;
	}
}
