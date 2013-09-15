package com.whereismytrain.transitboard;

import java.util.ArrayList;
import java.util.List;

import com.whereismytrain.transitboard.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class Directions extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_directions);
		//JSON stub here
		//hardcoded directions
		ListView directions = (ListView)findViewById(R.id.directions);
		List<String> list = new ArrayList<String>();
		list.add("Walk 315m to UQ Chancellor's Place, zone D");
		list.add("Catch express bus to Benson St at Toowong, stop 14 ");
		list.add("Walk 153m to Toowong station, platform 2");
		list.add("Catch train to Milton station");
		//fill the listview
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, list);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
		directions.setAdapter(arrayAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.directions, menu);
		return true;
	}
	//open map screen
	public void openMap(View view) {
		Intent i = new Intent(this, Map.class);
		 startActivity(i);
	}
	public String[] retrieveDirections(String routeId) {
		String[] directions;
		directions = new String[10];
		return directions;
		
		
	}

}
