package com.whereismytrain.transitboard.test;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;

import com.whereismytrain.transitboard.HomeScreen;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

public class MainScreenTest extends ActivityInstrumentationTestCase2<HomeScreen> {

	public MainScreenTest(Class<HomeScreen> activityClass) {
		super(activityClass);
		// TODO Auto-generated constructor stub
	}
	public MainScreenTest() {  
		super(HomeScreen.class);  
		}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public void testPreGetUserLocation() {
		HomeScreen homeScreen = getActivity();
		assertNull("location should be null", homeScreen.userLocation);
		
	}
	
	public void testGetUserLocation() {
		HomeScreen homeScreen = getActivity();
		homeScreen.getUserLocation();
		assertNotNull("location not obtained", homeScreen.userLocation);
		
		}
	
	public void testNoLocationFound() {
		HomeScreen homeScreen = getActivity();
		homeScreen.getUserLocation();
		homeScreen.userLocation = null;
		try {
			homeScreen.bindNearbyStops(homeScreen.userLocation);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TextView or = (TextView)homeScreen.findViewById(com.whereismytrain.transitboard.R.id.Or);
		assertTrue(or.isShown());
	}

}
