package com.neo.infocommunicate.protocol;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.neo.infocommunicate.data.MessageInfo;

import android.text.TextUtils;

public class ProtocolDataInput {

    public MessageInfo parseInfoFromJSON(String input) throws JSONException {
	if (input == null || TextUtils.isEmpty(input)) {
	    return null;
	}
	try {
	    JSONTokener jsonParser = new JSONTokener(input);
	    JSONObject info = (JSONObject) jsonParser.nextValue();
	    JSONObject info_params = info.getJSONObject("info_params");
	    if (info_params != null) {
		MessageInfo msg = new MessageInfo();
		msg.key = info_params.getString("key");
		msg.title = info_params.getString("title");
		msg.message = info_params.getString("message");
		msg.place = info_params.getString("place");
		msg.link = info_params.getString("link");
		msg.time = info_params.getLong("time");
		msg.receive_time = info_params.getLong("receive_time");
		return msg;
	    }
	    return null;
	} catch (JSONException ex) {
	    // 异常处理代码
	}
	return null;
    }
}
// {"user_params":{"channel_id":"3945703801488622863","user_id":"937562813467006556"},"info_params":{"key":"","title":"","message":"","place":"","link":"","time":,"receive_time":}}
