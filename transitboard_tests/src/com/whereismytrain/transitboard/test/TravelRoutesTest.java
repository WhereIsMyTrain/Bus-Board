package com.whereismytrain.transitboard.test;

import com.whereismytrain.transitboard.TravelRoutes;

import android.test.ActivityInstrumentationTestCase2;

public class TravelRoutesTest extends
		ActivityInstrumentationTestCase2<TravelRoutes> {

	public TravelRoutesTest(Class<TravelRoutes> activityClass) {
		super(activityClass);
		// TODO Auto-generated constructor stub
	}
	public TravelRoutesTest() {
		super(TravelRoutes.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	public void testGetTravelRoutes() {
		TravelRoutes travelRoutes = getActivity();
		String originStopId = "001824";
		String destinationStopId = "003420";
		String dateTime = "09092013+18";
		String[][] routes = null;
		try {
			routes = travelRoutes.
					retrieveTravelRoutes(originStopId, destinationStopId, dateTime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull("empty array", routes);
	}
	
	public void testInvalidIdInput() {
		TravelRoutes travelRoutes = getActivity();
		String originStopId = "999999";
		String destinationStopId = "000000";
		String dateTime = "09092013+18";
		String[][] routes = null;
		try {
			routes = travelRoutes.
					retrieveTravelRoutes(originStopId, destinationStopId, dateTime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNull("should return an empty array", routes);
	}
	public void testSameId() {
		TravelRoutes travelRoutes = getActivity();
		String originStopId = "001841";
		String destinationStopId = "001841";
		String dateTime = "09092013+18";
		String[][] routes = new String[10][];
		try {
			routes = travelRoutes.
					retrieveTravelRoutes(originStopId, destinationStopId, dateTime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNull("should return an empty array", routes);
	}
	public void testNullId() {
		TravelRoutes travelRoutes = getActivity();
		String originStopId = null;
		String destinationStopId = null;
		String dateTime = null;
		String[][] routes = new String[10][];
		try {
			routes = travelRoutes.
					retrieveTravelRoutes(originStopId, destinationStopId, dateTime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNull("should return an empty array", routes);
	}
}
