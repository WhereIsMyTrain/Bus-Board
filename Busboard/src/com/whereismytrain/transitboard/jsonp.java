package com.whereismytrain.transitboard;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.StrictMode;
import android.widget.Toast;
 
public class Jsonp {
 
    static InputStream is = null;
    static JSONObject JSON = null;
    static String json = "";
    
 
    // constructor
    public Jsonp() {
 
    }
 
    public JSONObject getJSONFromUrl(String url) {
    	//Uses the UI thread for HTTP access. Will change this to a Async method.
    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	StrictMode.setThreadPolicy(policy); 
        // Gets the http response and puts it in a input stream
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(new HttpGet(url));
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();          
        } catch (IOException e) {
            e.printStackTrace();
        }
        //BufferReader to read the input stream and to out put it into a string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            json = reader.readLine();
            reader.close();
            is.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    	//Convert the string into a JSON object.
        try {
			JSON = new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
        return JSON;
 
    }
}