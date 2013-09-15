package com.whereismytrain.transitboard;

import com.whereismytrain.transitboard.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Statistics extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.statistics, menu);
		return true;
	}
	
	public void updateDistance(int dist) {
		//updates variable in database to += dist
	}
	
	public int getTotalDistance() {
		int dist = 0;
		return dist;
	}
	public void setDistance(int dist) {
		int distance = dist;
	}
	
	public void resetDistance() {
		int distance = 0;
	}

}
