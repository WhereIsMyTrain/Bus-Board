package com.whereismytrain.transitboard.test;

import com.whereismytrain.transitboard.Directions;
import com.whereismytrain.transitboard.Status;

import android.test.ActivityInstrumentationTestCase2;

public class StatusTest extends ActivityInstrumentationTestCase2<Status> {

	public StatusTest(Class<Status> activityClass) {
		super(activityClass);
		// TODO Auto-generated constructor stub
	}
	public StatusTest() {
		super(Status.class);
	}
		
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testRetrieveTwitterFeed() {
		Status status = getActivity();
		String url = "https://twitter.com/TransLinkSEQ";
		String[] posts = null;
		status.retrieveTwitterFeed(url);
		assertNotNull("should have at least 0 posts", posts);
	}

}
