package com.neo.infocommunicate.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.neo.infocommunicate.controller.PersonManager;
import com.neo.infocommunicate.data.MessageInfo;
import com.neo.infocommunicate.data.NoticeInfo;
import com.neo.infocommunicate.data.SendNoticeInfo;
import com.neo.infocommunicate.data.UserInfo;
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

	// 解析服务器设置用户名
	public static String parseSetNickResultFromJSON(String input)
			throws JSONException {
		if (input == null || TextUtils.isEmpty(input)) {
			return null;
		}
		try {
			JSONTokener jsonParser = new JSONTokener(input);
			JSONObject obj = (JSONObject) jsonParser.nextValue();
			return obj.getString("nick_result");
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
				return null;
			PersonManager.getInstance().getReceiverList().clear();
			for (int i = 0; i < arrays.length(); i++) {
				UserInfo u = new UserInfo();
				JSONObject item =  (JSONObject) arrays.opt(i);
				u.user_id = item.getString("id");
				u.nick_name = item.getString("nick");
				PersonManager.getInstance().getReceiverList()
						.add(u);
			}
		} catch (JSONException ex) {
			// 异常处理代码
		} catch (Exception e) {

		}
		return null;
	}

	// 解析push notice 发送的结果
	public static String parseSendNoticeResultFromJSON(String input)
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

	// 解析push message 发送的结果
	public static String parseSendMessageResultFromJSON(String input)
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

	// 解析发送的push notice 信息
	public static SendNoticeInfo parseSendPushNoticeFromJSON(String input)
			throws JSONException {
		if (input == null || TextUtils.isEmpty(input)) {
			return null;
		}
		try {
			JSONTokener jsonParser = new JSONTokener(input);
			JSONObject info = (JSONObject) jsonParser.nextValue();
			if (info != null) {
				SendNoticeInfo msg = new SendNoticeInfo();
				msg.info = new NoticeInfo();
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

	// 解析收取的 push notice 信息
	public static NoticeInfo parsePushNoticeFromJSON(String input)
			throws JSONException {
		if (input == null || TextUtils.isEmpty(input)) {
			return null;
		}
		try {
			JSONTokener jsonParser = new JSONTokener(input);
			JSONObject info = (JSONObject) jsonParser.nextValue();
			if (info != null) {
				NoticeInfo msg = new NoticeInfo();
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

	// 解析收取的 push message 信息
	public static MessageInfo parsePushMessageFromJSON(String input)
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
				msg.sender_id = Utf8Code.utf8Decode(info.getString("sender_id"));
				msg.sender_nick = Utf8Code.utf8Decode(info.getString("sender_nick"));
				msg.receiver_id = Utf8Code.utf8Decode(info.getString("receiver_id"));
				msg.message = Utf8Code.utf8Decode(info.getString("message"));
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