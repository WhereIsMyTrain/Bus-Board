package com.whereismytrain.transitboard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Nearby extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearby);
		jsonp jParser = new jsonp();
		// Using a hardcoded url for simplicity for the tech spike
		String url = "http://deco3801-003.uqcloud.net/opia/location/rest/stops-nearby/AD:300%20sir%20fred%20schonell?radiusM=500&useWalkingDistance=true&maxResults=20";
		 
		// Gets the JSON from the url
		JSONObject json = jParser.getJSONFromUrl(url);
		try {
			//Splits the JSON file in each individual stop 
			JSONArray stops = json.getJSONArray("NearbyStops");
			 for(int i = 0; i < stops.length(); i++){
				JSONObject c = stops.getJSONObject(i);
				//Gets the StopId in the format 'XXXXXX'
				String id = c.getString("StopId");
				url = "http://deco3801-003.uqcloud.net/opia/location/rest/stops?ids=SI%3A"
						+ id;
				JSONObject stopDetails = jParser.getJSONFromUrl(url);
				JSONObject stopDetail = stopDetails.getJSONArray("Stops")
						.getJSONObject(0);
				//Gets relevant details for each stop
				String description = stopDetail.getString("Description");
				//String position = stopDetail.getString("Position");
				String zone = stopDetail.getString("Zone");
				//Gets the distance from the selected loction to the stop
				JSONObject d = c.getJSONObject("Distance");
				String distance = d.getString("DistanceM");
				//Builds the layout for stop details on two lines
				//Whitespace tabs used to show nesting of layouts
				LinearLayout masterLayout = (LinearLayout) findViewById(R.id.nearby);
				 	LinearLayout lineOne = new LinearLayout(this);
						TextView descriptionText = new TextView(this);
						descriptionText.setText("Stop: "+description);
					lineOne.addView(descriptionText);
				 	LinearLayout lineTwo = new LinearLayout(this);
				 	lineTwo.setOrientation(LinearLayout.HORIZONTAL);
				 	//Add a bottom margin to separate from the next stops details
				 	lineTwo.setPadding(0, 0, 0, 10);
						TextView distanceText = new TextView(this);
						distanceText.setText("Distance: "+distance+" metres");
						TextView zoneText = new TextView(this);
						zoneText.setText("Zone: "+zone);
						zoneText.setPadding(8, 0, 0, 0);
					lineTwo.addView(distanceText, 0);
					lineTwo.addView(zoneText, 1);
				masterLayout.addView(lineOne);
				masterLayout.addView(lineTwo);
			 }
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nearby, menu);
		return true;
	}

}
