package com.whereismytrain.transitboard;

import com.whereismytrain.transitboard.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Status extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.status, menu);
		return true;
	}
	
	public String[] retrieveTwitterFeed(String url) {
		return null;
		
	}

}
