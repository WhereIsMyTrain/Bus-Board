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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

public class Directions extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_directions);
		Intent intent = getIntent();
		
		String route = null;
		try {
			route = intent.getStringExtra(TravelRoutes.ROUTE);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString()
					, Toast.LENGTH_LONG).show();
		}
		
		if (route == null)
			home(new View(getApplicationContext()));
		
		try {
			displayDirections(route);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString()
					, Toast.LENGTH_LONG).show();
		}
		
	}
	
	public void home(View view) {
		Intent i = new Intent(this, HomeScreen.class);
		startActivity(i);
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
			int tMode = inst.getInt("TravelMode");
			
			
			ImageView image = new ImageView(getApplicationContext());
			switch (tMode) {
				case 2: image.setImageResource(R.drawable.bus);
				break;
				case 4: image.setImageResource(R.drawable.ferry);
				break;
				case 8: image.setImageResource(R.drawable.train);
				break;
				case 16: image.setImageResource(R.drawable.walk);
				break;
				}
			
			TextView dir = new TextView(this);
			TextView leg = new TextView(this);
			
			LayoutParams textParams = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			textParams.setMargins(6, 0, 0, 0);
			LayoutParams imageParams = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			imageParams.setMargins(3, 20, 0, 0);
			LayoutParams lineParams = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lineParams.setMargins(0, 10, 0, 40);
			
			image.setLayoutParams(imageParams);
			leg.setLayoutParams(imageParams);
			dir.setLayoutParams(textParams);
			
			ImageView line = new ImageView(getApplicationContext());
		 	line.setImageResource(R.drawable.line);
		 	line.setScaleType(ScaleType.FIT_XY);
			
		 	leg.setTextSize(35);
		 	leg.setText((j + 1) + ": ");
			dir.setText(inst.getString("Instruction"));
			dir.setPadding(0, 0, 0, 30);
			
			lineDir.addView(image);
			lineDir.addView(leg);
			lineDir.addView(dir);
			lineDir.setLayoutParams(lineParams);
			
			masterLayout.addView(lineDir);
			masterLayout.addView(line);
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
	public void mapScreen(View view) {
		Intent i = new Intent(this, Map.class);
		 startActivity(i);
	}
	public String[] retrieveDirections(String routeId) {
		String[] directions;
		directions = new String[10];
		return directions;
		
		
	}

}
