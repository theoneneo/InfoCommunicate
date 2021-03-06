package com.neo.infocommunicate.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.neo.tools.Utf8Code;

public class ProtocolDataOutput {
	public static String registerUserIdToJSON(String id) throws JSONException {
		try {
			JSONObject output = new JSONObject();
			output.put("register_id", Utf8Code.utf8Encode(id));
			return output.toString();
		} catch (JSONException ex) {
			throw new RuntimeException(ex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String loginUserIdToJSON(String id) throws JSONException {
		try {
			JSONObject output = new JSONObject();
			output.put("login_id", Utf8Code.utf8Encode(id));
			return output.toString();
		} catch (JSONException ex) {
			throw new RuntimeException(ex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String SetNickNameToJSON(String user_id, String nick)
			throws JSONException {
		try {
			JSONObject output = new JSONObject();
			output.put("user_id", Utf8Code.utf8Encode(user_id));
			output.put("nick_name", Utf8Code.utf8Encode(nick));
			return output.toString();
		} catch (JSONException ex) {
			throw new RuntimeException(ex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String sendPushNoticeToJSON(ArrayList<String> ids,
			String title, String message, String place, String link, String time)
			throws JSONException {
		try {
			JSONObject output = new JSONObject();
			JSONArray receives_id = new JSONArray();
			for (int i = 0; i < ids.size(); i++) {
				receives_id.put(Utf8Code.utf8Encode(ids.get(i)));
			}
			output.put("receives_id", receives_id);
			output.put("title", Utf8Code.utf8Encode(title));
			output.put("message", Utf8Code.utf8Encode(message));
			output.put("place", Utf8Code.utf8Encode(place));
			output.put("link", Utf8Code.utf8Encode(link));
			output.put("time", time);
			return output.toString();
		} catch (JSONException ex) {
			throw new RuntimeException(ex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String sendPushMessageToJSON(String sender_id,
			String sender_nick, String receiver_id, String message)
			throws JSONException {
		try {
			JSONObject output = new JSONObject();
			output.put("sender_id", Utf8Code.utf8Encode(sender_id));
			output.put("sender_nick", Utf8Code.utf8Encode(sender_nick));
			output.put("receiver_id", Utf8Code.utf8Encode(receiver_id));
			output.put("message", Utf8Code.utf8Encode(message));
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
