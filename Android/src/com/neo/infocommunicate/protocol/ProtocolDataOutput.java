package com.neo.infocommunicate.protocol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProtocolDataOutput {
	public String sendPushMessageToJSON(String[] ids, String title,
			String message, String place, String link, String time)
			throws JSONException {
		try {
			JSONObject output = new JSONObject();
			JSONArray receives_id = new JSONArray();
			for (int i = 0; i < ids.length; i++) {
				receives_id.put(ids[i]);
			}
			output.put("receives_id", receives_id);
			output.put("title", title);
			output.put("message", message);
			output.put("place", place);
			output.put("link", link);
			output.put("time", time);
			return output.toString();
		} catch (JSONException ex) {
			throw new RuntimeException(ex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String saveUserIdToJSON(String id)
			throws JSONException {
		try {
			JSONObject output = new JSONObject();
			output.put("user_id", id);
			return output.toString();
		} catch (JSONException ex) {
			throw new RuntimeException(ex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
