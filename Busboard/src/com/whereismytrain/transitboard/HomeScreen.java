package com.whereismytrain.transitboard;

import com.whereismytrain.transitboard.R;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class HomeScreen extends Activity {
	public final static String E_DESTINATION = "com.whereismytrain.transitboard.DESTINATION";
	public final static String ORIGIN_ID = "com.whereismytrain.transitboard.ORIGIN";
	public final static String LEAVE_WHEN = "com.whereismytrain.transitboard.LEAVE";
	public final static String DATE_TIME = "com.whereismytrain.transitboard.DATE";
	public final static String TEST = "com.whereismytrain.transitboard.TEST";
	private static List<String> idList = new ArrayList<String>();
	private static List<String> locations = new ArrayList<String>();
	public Location userLocation;
	private Intent routeIntent;
	private Object[] toLocNames = null;
	private Object[] fromLocNames = null;
	
	private Handler toHandler = new Handler() {
		  @Override
		  public void handleMessage(Message msg) {
			  AutoCompleteTextView to = (AutoCompleteTextView) findViewById(R.id.To);
	        	ArrayAdapter<Object> adapter = 
	        			new ArrayAdapter<Object>(
	        					getApplicationContext(), android.R.layout.simple_dropdown_item_1line, toLocNames);

	        	to.setAdapter(adapter);
			  //ProgressBar bar = (ProgressBar)findViewById(R.id.progressBar1);
			  //bar.setVisibility(View.INVISIBLE);
			  
		     }
		 };
	 Handler fromHandler = new Handler() {
		  @Override
		  public void handleMessage(Message msg) {
			  AutoCompleteTextView from = (AutoCompleteTextView) findViewById(R.id.From);
	        	ArrayAdapter<Object> adapter = 
	        			new ArrayAdapter<Object>(
	        					getApplicationContext(), android.R.layout.simple_dropdown_item_1line, fromLocNames);

	        	from.setAdapter(adapter);
			  //ProgressBar bar = (ProgressBar)findViewById(R.id.progressBar1);
			  //bar.setVisibility(View.INVISIBLE);
			  
		     }
		 };
		 
	LocationListener locationListener = new LocationListener() {
	    public void onLocationChanged(Location location) {
	      // Called when a new location is found by the network location provider.
	    	userLocation = location;
	 
		    Toast.makeText(getApplicationContext(), userLocation.getLatitude() + 
		  		  ", " + userLocation.getLongitude() , Toast.LENGTH_LONG).show();
		    

			EditText from = (EditText) findViewById(R.id.From);
			//from.setText(location.getLongitude() + "," + location.getLatitude());
		    /*try {
				bindNearbyStops(userLocation);
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
	    }

	    public void onStatusChanged(String provider, int status, Bundle extras) {}

	    public void onProviderEnabled(String provider) {}

	    public void onProviderDisabled(String provider) {}
	  };
	  
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);
		addLis();
		leaveArriveSpinner();
		
		//starts up the location listener which updates the variable userLocation
		getUserLocation();
		
		/*if (userLocation != null) {
			try {
				bindNearbyStops(userLocation);
			} catch (NullPointerException e) {
			} catch (JSONException e) {
			}
		}*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_screen, menu);
		return true;
		
	}
	public void addLis() {

		AutoCompleteTextView to = (AutoCompleteTextView) findViewById(R.id.To);
		AutoCompleteTextView from = (AutoCompleteTextView) findViewById(R.id.From);
		to.addTextChangedListener(new TextWatcher(){
			public String st = "";
	     
	        public void beforeTextChanged(CharSequence s, int start, int count, int after){}
	        public void onTextChanged(CharSequence s, int start, int before, int count){
	        	
	        	st = s.toString();
	        	Runnable runnable = new Runnable() {
	    	        public void run() {     	
	    	        	synchronized (this) {
	    		        	try {
	    		        		toLocNames = resolveLocationDesc(st.toString());
	    						
	    					} catch (Exception e1) {
	    						Toast.makeText(getApplicationContext(), e1.toString(), 
	    								Toast.LENGTH_LONG).show();
	    					}
	    	        	}
	    	        	toHandler.sendEmptyMessage(0);
	    	        }
	        	};
	        	Thread mythread = new Thread(runnable);
	        	mythread.start();
	        }
			@Override
			public void afterTextChanged(Editable s) {
			}
	    });
		
		from.addTextChangedListener(new TextWatcher(){
			public String st = "";
	        public void beforeTextChanged(CharSequence s, int start, int count, int after){}
	        public void onTextChanged(CharSequence s, int start, int before, int count){
	        	st = s.toString();
	        	Runnable runnable = new Runnable() {
	    	        public void run() {     	
	    	        	synchronized (this) {
	    		        	try {
	    		        		fromLocNames = resolveLocationDesc(st.toString());
	    						
	    					} catch (Exception e1) {
	    						Toast.makeText(getApplicationContext(), e1.toString(), 
	    								Toast.LENGTH_LONG).show();
	    					}
	    	        	}
	    	        	fromHandler.sendEmptyMessage(0);
	    	        }
	        	};
	        	Thread mythread = new Thread(runnable);
	        	mythread.start();
	        }
			@Override
			public void afterTextChanged(Editable s) {
			}
	    });
	}
	
	public void leaveArriveSpinner() {
		Spinner time = (Spinner)findViewById(R.id.timeSpinner);
		List<String> aList = new ArrayList<String>();
		aList.add("Leave After");
		aList.add("Arrive Before");
		ArrayAdapter<String> adapterTime = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, aList);
		adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		time.setAdapter(adapterTime);
		
	}
	public static Object[] resolveLocationDesc(String input) throws NullPointerException, JSONException {
		//returns an array of descriptions
		
		String format = input.replace(' ', '+');
		Object[] resolved = null;
		List<String> places = new ArrayList<String>();
		Jsonp jParser = new Jsonp();
		String url = "http://deco3801-003.uqcloud.net/opia/location/rest/resolve?" +
				"input=" + format + "&filter=0&maxResults=20&api_key=special-key";
		//http://deco3801-003.uqcloud.net/opia/location/rest/resolve?input=Milton&filter=0&maxResults=20&api_key=special-key"
		JSONObject json = jParser.getJSONFromUrl(url);
		JSONArray locations = json.getJSONArray("Locations");
		for (int i = 0; i <locations.length(); i++) {
			JSONObject l = locations.getJSONObject(i);
			places.add(l.getString("Description"));
		}
		resolved = places.toArray();
		return resolved;
	}
public static Object[] resolveLocationId(String input) throws NullPointerException, JSONException {
		//returns an array of ids
		String format = input.replace(' ', '+');
		Object[] resolved = null;
		List<String> places = new ArrayList<String>();
		Jsonp jParser = new Jsonp();
		String url = "http://deco3801-003.uqcloud.net/opia/location/rest/resolve?" +
				"input=" + format + "&filter=0&maxResults=20&api_key=special-key";
		//http://deco3801-003.uqcloud.net/opia/location/rest/resolve?input=Milton&filter=0&maxResults=20&api_key=special-key"
		JSONObject json = jParser.getJSONFromUrl(url);
		JSONArray locations = json.getJSONArray("Locations");
		for (int i = 0; i <locations.length(); i++) {
			JSONObject l = locations.getJSONObject(i);
			places.add(l.getString("Id"));
		}
		resolved = places.toArray();
		return resolved;
	}
	
	
	public void bindNearbyStops(Location location) throws NullPointerException, JSONException{
		Jsonp jParser = new Jsonp();
		//Much of this function may be moved server side so that only one HTTP request is made
		// Using a hardcoded url for simplicity for the tech spike
		//https://opia.api.translink.com.au/v1/content/swaggerui/index.aspx
		//murphy.michael
		//0f82-5eU585N
		//AD:300%20sir%20fred%20schonell?
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
		
		if (userLocation != null) {
		
			for(int i = 0; i < stops.length(); i++){
				JSONObject c = stops.getJSONObject(i);
				//Gets the StopId in the format 'XXXXXX'
				String id = c.getString("StopId");
				idList.add(id);
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
	}
	
	public void retrieveCurrentLocation(Location location) {
		String latitude = String.valueOf(location.getLatitude());
		String longitude = String.valueOf(location.getLongitude());
		
	}

	public void getUserLocation() {
		Boolean isGPSEnabled;
		Boolean isNetworkEnabled;
		//LatLng location;
		LocationManager locationManager = 
				(LocationManager)getSystemService(Context.LOCATION_SERVICE);
		isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
		isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		
		if (userLocation == null)
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 20000, 10, locationListener);
		if (userLocation == null)
				locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 20000, 10, locationListener);
	
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000, 10, locationListener);
				
		if (userLocation != null){
			locationManager.removeUpdates(locationListener);
		}
		
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
		
		EditText dest = (EditText)findViewById(R.id.To);
		Spinner leave = (Spinner)findViewById(R.id.timeSpinner);
		TimePicker timeP = (TimePicker)findViewById(R.id.timePicker1);
		EditText origin = (EditText)findViewById(R.id.From);
		
		
		String destination = dest.getText().toString();
		String originId = origin.getText().toString();
		//String originId = idList.get(origin.getSelectedItemPosition());
		String when = Integer.toString(leave.getSelectedItemPosition());
		String time = 	Calendar.getInstance().get(Calendar.YEAR) + "+" + 
						(Calendar.getInstance().get(Calendar.MONTH) + 1) + "+" + 
						Calendar.getInstance().get(Calendar.DATE) + "+" +
						timeP.getCurrentHour() + "%3A" + timeP.getCurrentMinute();
		
		
		routeIntent = new Intent(this, TravelRoutes.class);
		routeIntent.putExtra(E_DESTINATION, destination);
		routeIntent.putExtra(ORIGIN_ID, originId);
		routeIntent.putExtra(LEAVE_WHEN, when);
		routeIntent.putExtra(DATE_TIME, time);
		
		startActivity(routeIntent);
	}
	
	

}
