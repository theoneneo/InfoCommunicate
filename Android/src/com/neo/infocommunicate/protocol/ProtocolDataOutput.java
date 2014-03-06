package com.neo.infocommunicate.protocol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProtocolDataOutput {
    public String textsToJSON() throws JSONException {
	try {
	    JSONObject output = new JSONObject();
	    JSONArray receives_id = new JSONArray();
	    receives_id.put("12345678").put("87654321");
	    output.put("receives_id", receives_id);

	    output.put("title", "title");
	    output.put("message", "message");
	    output.put("place", "place");
	    output.put("link", "link");
	    return output.toString();
	} catch (JSONException ex) {
	    throw new RuntimeException(ex);
	}
    }
}
