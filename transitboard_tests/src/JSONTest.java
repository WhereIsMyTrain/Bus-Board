import org.json.JSONObject;

import com.whereismytrain.transitboard.Jsonp;

import junit.framework.TestCase;


public class JSONTest extends TestCase {

	public JSONTest(String jsonp) {
		super(jsonp);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testJSONParsing() {

	   // test JSON parser
	   Jsonp jsonp = new Jsonp();
	   JSONObject obj = null;
	   String url = "http://deco3801-003.uqcloud.net/opia/location/" +
			"rest/stops-nearby/AD:300%20sir%20fred%20schonell?" +
			"radiusM=500&useWalkingDistance=true&maxResults=20";
	   obj = jsonp.getJSONFromUrl(url);
	   // test that it returns a
	   assertNotNull("Parsing failed", obj);
	 } 

}
