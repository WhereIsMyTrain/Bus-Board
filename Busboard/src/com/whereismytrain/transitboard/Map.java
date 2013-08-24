package com.whereismytrain.transitboard;

import com.whereismytrain.transitboard.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public class Map extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
	}
    
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

}
