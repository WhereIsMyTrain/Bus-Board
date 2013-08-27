package com.whereismytrain.transitboard;

import com.google.android.gms.maps.SupportMapFragment;
import com.whereismytrain.transitboard.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.*;

import java.io.*;
 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

 
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;


public class HomeScreen extends Activity {

	public static Object[][] busstops = {
		{1882, "UQ Lakes station", -27.49805, 153.018077 , 2, "http://translink.com.au/stop/001882"},
		{1801,"UQ Chancellor's Place", -27.498448, 153.011036, 2, "http://translink.com.au/stop/001801"}
	}; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);
		TextView or = (TextView)findViewById(R.id.Or);
		
		
		Spinner from = (Spinner)findViewById(R.id.From);
		List<String> list = new ArrayList<String>();
		list.add("From: " + busstops[0][1].toString());
		list.add("From: " + busstops[1][1].toString());
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		from.setAdapter(adapter);
		
		Spinner time = (Spinner)findViewById(R.id.timeSpinner);
		List<String> aList = new ArrayList<String>();
		aList.add("Leave After");
		aList.add("Arrive Before");
		ArrayAdapter<String> adapterTime = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, aList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		time.setAdapter(adapterTime);
		
		
		
		String[] location;
		String suburb;
		JSONParser parser = new JSONParser();
		 
		try {
	 
			Object obj = parser.parse("http://deco3801-003.uqcloud.net/opia/location/rest/resolve?input=st+lucia&filter=3&maxResults=20");
	 
			JSONObject jsonObject = (JSONObject) obj;
	 
			location = (String[]) jsonObject.get("Locations");
			or.setText(location[0], TextView.BufferType.EDITABLE);
			//suburb = (String) jsonObject.get("Suburb");
			
		} catch (ParseException e) {
			e.printStackTrace();
		}

		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_screen, menu);
		return true;
		
	}
	public void mapScreen(View view) {
		Intent i = new Intent(this, Map.class);
		 startActivity(i);
	}
	
	

}
