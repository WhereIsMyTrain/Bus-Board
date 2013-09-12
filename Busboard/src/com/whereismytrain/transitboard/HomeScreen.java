package com.whereismytrain.transitboard;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.whereismytrain.transitboard.R;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
 
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;


public class HomeScreen extends Activity {

	//JSON Parser should spurt something like this out:
	public static Object[][] busstops = {
		{1882, "UQ Lakes station", -27.49805, 153.018077 , 2, "http://translink.com.au/stop/001882"},
		{1801,"UQ Chancellor's Place", -27.498448, 153.011036, 2, "http://translink.com.au/stop/001801"}
	}; 
	private Intent routeIntent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);
		TextView or = (TextView)findViewById(R.id.Or);
		
		Location location = getUserLocation();
		
		try {
			bindNearbyStops(location);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Spinner time = (Spinner)findViewById(R.id.timeSpinner);
		List<String> aList = new ArrayList<String>();
		aList.add("Leave After");
		aList.add("Arrive Before");
		ArrayAdapter<String> adapterTime = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, aList);
		adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		time.setAdapter(adapterTime);
		
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_screen, menu);
		return true;
		
	}
	
	public void bindNearbyStops(Location location) throws NullPointerException, JSONException{
		jsonp jParser = new jsonp();
		//Much of this function may be moved server side so that only one HTTP request is made
		// Using a hardcoded url for simplicity for the tech spike
		//https://opia.api.translink.com.au/v1/content/swaggerui/index.aspx
		//murphy.michael
		//0f82-5eU585N
		//AD:300%20sir%20fred%20schonell?
		location = getUserLocation();
		String latitude = String.valueOf(location.getLatitude());
		String longitude = String.valueOf(location.getLongitude());
		
		String url = "http://deco3801-003.uqcloud.net/opia/location/" +
				"rest/stops-nearby/GP:" + latitude + ",%20" + longitude + "?" +
				"radiusM=500&useWalkingDistance=true&maxResults=20";
		//http://deco3801-003.uqcloud.net/opia/location/rest/stops-nearby/AD:300%20sir%20fred%20schonell?radiusM=500&useWalkingDistance=true&maxResults=20
		// Gets the JSON from the url
		JSONObject json = jParser.getJSONFromUrl(url);
			//Splits the JSON file in each individual stop 
		JSONArray stops = json.getJSONArray("NearbyStops");
		Spinner from = (Spinner)findViewById(R.id.From);
		List<String> list = new ArrayList<String>();
		
		for(int i = 0; i < stops.length(); i++){
			JSONObject c = stops.getJSONObject(i);
			//Gets the StopId in the format 'XXXXXX'
			String id = c.getString("StopId");
			url = "http://deco3801-003.uqcloud.net/opia/location/rest/stops?ids=SI%3A"
					+ id;
			//Look to move this step to Server side to reduce the number of HTTP request the clients makes
			JSONObject stopDetails = jParser.getJSONFromUrl(url);
			JSONObject stopDetail = stopDetails.getJSONArray("Stops")
					.getJSONObject(0);
			//Gets relevant details for each stop
			String description = stopDetail.getString("Description");
			//String position = stopDetail.getString("Position");
			String zone = stopDetail.getString("Zone");
			//Gets the distance from the selected location to the stop
			JSONObject d = c.getJSONObject("Distance");
			String distance = d.getString("DistanceM");
			//Builds the layout for stop details on two lines
			//Whitespace tabs used to show nesting of layouts
			
			list.add(description + ": " + distance + "m, zone: " + zone);
		 }
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this, R.layout.textview, list);
		adapter.setDropDownViewResource(R.layout.textview);
		from.setAdapter(adapter);
	}
	
	public Location getUserLocation() {
		Location location;
		Boolean isGPSEnabled;
		Boolean isNetworkEnabled;
		//LatLng location;
		LocationManager locationManager = 
				(LocationManager)getSystemService(Context.LOCATION_SERVICE);
		isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
		isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		/*
		if (isGPSEnabled) {
			location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		} else if (isNetworkEnabled) {
			location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		} else
			location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
		*/
		Toast.makeText(getApplicationContext(), location.getLatitude() + ", " + location.getLongitude() , Toast.LENGTH_LONG).show();

		
		return location;
	}
	//open map
	public void mapScreen(View view) {
		Intent i = new Intent(this, Map.class);
		 startActivity(i);
	}
	
	public void nearby(View view) {
		Intent i = new Intent(this, Nearby.class);
		 startActivity(i);
	}
	
	public void routes(View view) {
		routeIntent = new Intent(this, TravelRoutes.class);
		startActivity(routeIntent);
	}
	
	

}
