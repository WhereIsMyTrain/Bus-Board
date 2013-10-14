package com.whereismytrain.transitboard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.whereismytrain.transitboard.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Directions extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_directions);
		Intent intent = getIntent();
		
		String route = "null";
		try {
			route = intent.getStringExtra(TravelRoutes.ROUTE);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString()
					, Toast.LENGTH_LONG).show();
		}
		
		try {
			displayDirections(route);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString()
					, Toast.LENGTH_LONG).show();
		}
		
	}
	
	public void displayDirections(String directions) throws JSONException{
		JSONObject itin = new JSONObject(directions);
		JSONArray legs = itin.getJSONArray("Legs");
		LinearLayout masterLayout = (LinearLayout) findViewById(R.id.directions);
		Long date = System.currentTimeMillis();
		for (int j=0; j < legs.length(); j++) {
			
			LinearLayout lineDir = new LinearLayout(this);
			JSONObject inst = legs.getJSONObject(j);
			String depTime = inst.getString("DepartureTime");
			String timeRem = timeRemaining(depTime, date);
			TextView dir = new TextView(this);
			dir.setText("Leg " + (j + 1) + ": Departs in " + timeRem + 
					". " + inst.getString("Instruction"));
			lineDir.addView(dir);
			masterLayout.addView(lineDir);
		}
	}

	public String timeRemaining(String depDate, Long now) {
		Long departure = Long.parseLong(depDate.substring(6,19));
		Long difference = departure - now;
		Date date = new Date(difference);
		
		SimpleDateFormat formatter = new SimpleDateFormat("mm");
		return formatter.format(date);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.directions, menu);
		return true;
	}
	//open map screen
	public void openMap(View view) {
		Intent i = new Intent(this, Map.class);
		 startActivity(i);
	}
	public String[] retrieveDirections(String routeId) {
		String[] directions;
		directions = new String[10];
		return directions;
		
		
	}

}
