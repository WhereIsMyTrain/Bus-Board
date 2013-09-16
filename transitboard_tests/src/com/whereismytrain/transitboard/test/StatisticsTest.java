package com.whereismytrain.transitboard.test;

import com.whereismytrain.transitboard.Statistics;

import android.test.ActivityInstrumentationTestCase2;

public class StatisticsTest extends
		ActivityInstrumentationTestCase2<Statistics> {

	public StatisticsTest(Class<Statistics> activityClass) {
		super(activityClass);
		// TODO Auto-generated constructor stub
	}
	public StatisticsTest() {
		super(Statistics.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testRetrieveDistance() {
		Statistics statistics = getActivity();
		int totalDist;
		totalDist = statistics.getTotalDistance();
		assertNotNull(totalDist);
	}
	public void testUpdateDistance() {
		Statistics statistics = getActivity();
		int expectedAns = 2300;
		int newDist = 2300;
		int currentDist;
		statistics.setDistance(0);
		statistics.updateDistance(newDist);
		currentDist = statistics.getTotalDistance();
		assertEquals(expectedAns, currentDist);
	}
	
	public void testResetDistance() {
		Statistics statistics = getActivity();
		statistics.resetDistance();
		int dist = statistics.getTotalDistance();
		assertEquals(0, dist);
	}

}
