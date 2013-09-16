package com.whereismytrain.transitboard.test;

import com.whereismytrain.transitboard.Directions;
import com.whereismytrain.transitboard.TravelRoutes;

import android.test.ActivityInstrumentationTestCase2;

public class DirectionsTest extends ActivityInstrumentationTestCase2<Directions> {

	public DirectionsTest(Class<Directions> activityClass) {
		super(activityClass);
		// TODO Auto-generated constructor stub
	}
	public DirectionsTest() {
		super(Directions.class);
	}
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testRetrieveDirections() {
		Directions directions = getActivity();
		String[] theseDirections = new String[10];
		String routeId = "001841";
		theseDirections = directions.retrieveDirections(routeId);
		assertTrue("no directions were returned", theseDirections.length > 0);
	}
	
	public void testInvalidRouteId() {
		Directions directions = getActivity();
		String[] theseDirections = new String[10];
		String routeId = "000000";
		theseDirections = directions.retrieveDirections(routeId);
		assertTrue("we want an empty array not an error", theseDirections.length == 0);
	
	}
	public void testNullRouteId() {
		Directions directions = getActivity();
		String[] theseDirections = new String[10];
		String routeId = null;
		theseDirections = directions.retrieveDirections(routeId);
		assertTrue("we want an empty array not an error", theseDirections.length == 0);
	
	}

}
