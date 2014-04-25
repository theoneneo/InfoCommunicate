package com.neo.infocommunicate.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProtocolDataOutput {
	public static String registerUserIdToJSON(String id)
			throws JSONException {
		try {
			JSONObject output = new JSONObject();
			output.put("register_id", id);
			return output.toString();
		} catch (JSONException ex) {
			throw new RuntimeException(ex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String loginUserIdToJSON(String id)
			throws JSONException {
		try {
			JSONObject output = new JSONObject();
			output.put("login_id", id);
			return output.toString();
		} catch (JSONException ex) {
			throw new RuntimeException(ex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String sendPushMessageToJSON(ArrayList<String> ids, String title,
			String message, String place, String link, String time)
			throws JSONException {
		try {
			JSONObject output = new JSONObject();
			JSONArray receives_id = new JSONArray();
			for (int i = 0; i < ids.size(); i++) {
				receives_id.put(ids.get(i));
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
	
	public static String getReceiverListToJSON(String flag)
			throws JSONException {
		try {
			JSONObject output = new JSONObject();
			output.put("get_receiver_list", flag);
			return output.toString();
		} catch (JSONException ex) {
			throw new RuntimeException(ex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
