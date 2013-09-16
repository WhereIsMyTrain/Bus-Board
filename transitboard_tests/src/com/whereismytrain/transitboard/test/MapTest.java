package com.whereismytrain.transitboard.test;

import com.whereismytrain.transitboard.Map;

import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;

public class MapTest extends ActivityInstrumentationTestCase2<Map> {

	public MapTest(Class<Map> activityClass) {
		super(activityClass);
		// TODO Auto-generated constructor stub
	}
	public MapTest() {
		super(Map.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testfindNearestStops() {
		Map map = getActivity();
		//uq coordinates
		Double latitude = -27.4986;
		Double longitude = 153.0155;
		Location[] nearbyStops;
		nearbyStops = map.findNearestStops(latitude, longitude);
		assertTrue("should return at least 2 stops", nearbyStops.length > 2);
	}
	
	public void testNoNearbyStops() {
		Map map = getActivity();
		//coordinates out in the ocean
		Double latitude = 27.4986;
		Double longitude = 153.0155;
		Location[] nearbyStops =  null;
		nearbyStops = map.findNearestStops(latitude, longitude);
		assertNotNull("should return an empty array", nearbyStops);
	}

}
