package com.neo.infocommunicate.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.neo.infocommunicate.controller.PersonManager;
import com.neo.infocommunicate.data.MessageInfo;
import com.neo.infocommunicate.data.SendMessageInfo;
import com.neo.tools.Utf8Code;

import android.text.TextUtils;

public class ProtocolDataInput {

	public ProtocolDataInput() {

	}

	// 解析服务器注册结果
	public static String parseRegisterResultFromJSON(String input)
			throws JSONException {
		if (input == null || TextUtils.isEmpty(input)) {
			return null;
		}
		try {
			JSONTokener jsonParser = new JSONTokener(input);
			JSONObject obj = (JSONObject) jsonParser.nextValue();
			return obj.getString("register_result");
		} catch (JSONException ex) {
			// 异常处理代码
		} catch (Exception e) {

		}
		return null;
	}

	// 解析服务器登录结果
	public static String parseLoginResultFromJSON(String input)
			throws JSONException {
		if (input == null || TextUtils.isEmpty(input)) {
			return null;
		}
		try {
			JSONTokener jsonParser = new JSONTokener(input);
			JSONObject obj = (JSONObject) jsonParser.nextValue();
			return obj.getString("login_result");
		} catch (JSONException ex) {
			// 异常处理代码
		} catch (Exception e) {

		}
		return null;
	}

	// 解析服务器获取联系人列表
	public static String parseReceiverListFromJSON(String input)
			throws JSONException {
		if (input == null || TextUtils.isEmpty(input)) {
			return null;
		}
		try {
			JSONTokener jsonParser = new JSONTokener(input);
			JSONObject obj = (JSONObject) jsonParser.nextValue();
			JSONArray arrays = obj.getJSONArray("receiver_list");
			if (arrays == null)
				return obj.getString("receiver_list");
			for (int i = 0; i < arrays.length(); i++) {
				PersonManager.getInstance().getReceiverList()
						.add((String) arrays.opt(i));
			}
		} catch (JSONException ex) {
			// 异常处理代码
		} catch (Exception e) {

		}
		return null;
	}

	// 解析push 发送的结果
	public static String parseSendResultFromJSON(String input)
			throws JSONException {
		if (input == null || TextUtils.isEmpty(input)) {
			return null;
		}
		try {
			JSONTokener jsonParser = new JSONTokener(input);
			JSONObject info = (JSONObject) jsonParser.nextValue();
			if (info != null) {
				return info.getString("send_result");
			}
		} catch (JSONException ex) {
			// 异常处理代码
		} catch (Exception e) {

		}
		return null;
	}

	// 解析push 发送的信息
	public static SendMessageInfo parseSendPushResultFromJSON(String input)
			throws JSONException {
		if (input == null || TextUtils.isEmpty(input)) {
			return null;
		}
		try {
			JSONTokener jsonParser = new JSONTokener(input);
			JSONObject info = (JSONObject) jsonParser.nextValue();
			if (info != null) {
				SendMessageInfo msg = new SendMessageInfo();
				msg.info = new MessageInfo();
				msg.info.key = info.getString("key");
				msg.info.title = info.getString("title");
				msg.info.message = info.getString("message");
				msg.info.place = info.getString("place");
				msg.info.link = info.getString("link");
				msg.info.time = info.getLong("time");
				JSONArray arrays = info.getJSONArray("receiver_list");
				if (arrays != null) {
					msg.receiver_list = new ArrayList<String>();
					for (int i = 0; i < arrays.length(); i++) {
						msg.receiver_list.add((String) arrays.opt(i));
					}
				}
				return msg;
			}
		} catch (JSONException ex) {
			// 异常处理代码
		} catch (Exception e) {

		}
		return null;
	}

	// 解析push 收取的信息
	public static MessageInfo parseInfoFromJSON(String input)
			throws JSONException {
		if (input == null || TextUtils.isEmpty(input)) {
			return null;
		}
		try {
			JSONTokener jsonParser = new JSONTokener(input);
			JSONObject info = (JSONObject) jsonParser.nextValue();
			if (info != null) {
				MessageInfo msg = new MessageInfo();
				msg.key = info.getString("key");
				msg.title = Utf8Code.utf8Decode(info.getString("title"));
				msg.message = Utf8Code.utf8Decode(info.getString("message"));
				msg.place = Utf8Code.utf8Decode(info.getString("place"));
				msg.link = Utf8Code.utf8Decode(info.getString("link"));
				msg.time = Long.valueOf(info.getString("time"));
				return msg;
			}
			return null;
		} catch (JSONException ex) {
			// 异常处理代码
		} catch (Exception e) {

		}
		return null;
	}
}