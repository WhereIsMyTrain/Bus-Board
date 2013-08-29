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
		String url = "http://deco3801-003.uqcloud.net/opia/location/rest/stops-nearby/AD:300%20sir%20fred%20schonell?radiusM=500&useWalkingDistance=true&maxResults=20";
		 
		// getting JSON string from URL
		JSONObject json = jParser.getJSONFromUrl(url);
		try {
			JSONArray stops = json.getJSONArray("NearbyStops");
//					TextView textName = (TextView) findViewById(R.id.textView2);
			LinearLayout A = (LinearLayout) findViewById(R.id.nearby);
			 for(int i = 0; i < stops.length(); i++){
				JSONObject c = stops.getJSONObject(i);
				String id = c.getString("StopId");
				url = "http://deco3801-003.uqcloud.net/opia/location/rest/stops?ids=SI%3A"
						+ id;
				JSONObject stopDetails = jParser.getJSONFromUrl(url);
				JSONObject stopDetail = stopDetails.getJSONArray("Stops")
						.getJSONObject(0);
				String street = stopDetail.getString("Description");
				String position = stopDetail.getString("Position");
				String zone = stopDetail.getString("Zone");
				JSONObject d = c.getJSONObject("Distance");
				String distance = d.getString("DistanceM");
			 	LinearLayout lineOne = new LinearLayout(this);
			 	lineOne.setOrientation(LinearLayout.HORIZONTAL);
				TextView test = new TextView(this);
				test.setText("Stop: "+street);
				lineOne.addView(test, 0);
			 	LinearLayout lineTwo = new LinearLayout(this);
			 	lineTwo.setOrientation(LinearLayout.HORIZONTAL);
			 	lineTwo.setPadding(0, 0, 0, 8);
				TextView test2 = new TextView(this);
				test2.setText("Distance: "+distance+" metres");
				lineTwo.addView(test2, 0);
				TextView test3 = new TextView(this);
				test3.setText("Zone: "+zone);
				test3.setPadding(8, 0, 8, 0);
				lineTwo.addView(test3, 1);
				A.addView(lineOne);
				A.addView(lineTwo);
			 }
//					textName.setText(String.valueOf(contacts.length()));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
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
