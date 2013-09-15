package com.whereismytrain.transitboard;

import com.whereismytrain.transitboard.R;

import android.widget.Toast;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends FragmentActivity {
	static final LatLng STLUCIA = new LatLng(-27.4986, 153.0155);
	static final LatLng ChancellorsPlace = new LatLng(-27.498448,153.011036);
	static final LatLng UQLakes = new LatLng(-27.49805,153.018077);
	private GoogleMap map;
	LatLng myPosition;
	// The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
 
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    float[] CPresults = new float[2];
    float[] UQLresults = new float[2];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		//find the map fragment
		SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
		map = fm.getMap();
		map.setMyLocationEnabled(true);
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);
        
        if (provider.equals("") || provider == null) {
        	Toast toast = Toast.makeText(getApplicationContext(), "no provider", Toast.LENGTH_SHORT);
        	toast.show();
        }

        // Getting Current Location
        Location location = locationManager.getLastKnownLocation(provider);

        if(location!=null){
        	// Getting latitude of the current location
        	double latitude = location.getLatitude();

        	// Getting longitude of the current location
        	double longitude = location.getLongitude();
        	//calculate distance between current location and bustop
        	Location.distanceBetween(latitude, longitude, UQLakes.latitude, UQLakes.longitude, UQLresults);
        	Location.distanceBetween(latitude, longitude, ChancellorsPlace.latitude, ChancellorsPlace.longitude, CPresults);
	        // Creating a LatLng object for the current location
	        myPosition = new LatLng(latitude, longitude);
	
	        map.addMarker(new MarkerOptions().position(ChancellorsPlace)
	        		.title("Chancellors Place Dist: " + CPresults[0] + "m"));
	        map.addMarker(new MarkerOptions().position(UQLakes)
	        		.title("UQ Lakes Dist: " + UQLresults[0] + "m"));
	        //map.addMarker(new MarkerOptions().position(myPosition).title("Nearest Busstop: " + results[0] + "m"));
        }
		
		// Move the camera instantly to my position with a zoom of 15.
	    map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 15));

	    // Zoom in, animating the camera.
	    //map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

	}
	
	public Location[] findNearestStops(Double latitude, Double longitude) {
		Location[] stopLocations = new Location[10];
		return stopLocations;
	}
	//Open the directions screen
	public void openDirections(View view) {
		Intent i = new Intent(this, Directions.class);
		 startActivity(i);
	}
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

}
