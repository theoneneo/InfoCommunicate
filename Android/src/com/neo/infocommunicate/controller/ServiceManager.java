package com.neo.infocommunicate.controller;

import java.util.ArrayList;

import org.json.JSONException;

import com.neo.infocommunicate.InfoCommApp;
import com.neo.infocommunicate.protocol.ProtocolDataOutput;
import com.neo.infocommunicate.task.GetReceiverListTask;
import com.neo.infocommunicate.task.LoginTask;
import com.neo.infocommunicate.task.RegisterTask;
import com.neo.infocommunicate.task.SendPushMessageTask;
import com.neo.infocommunicate.task.SendPushNoticeTask;
import com.neo.infocommunicate.task.SetNickNameTask;
import com.neo.tools.Utf8Code;

/**
 * @author LiuBing
 * @version 2014-3-6 下午4:33:24
 */
public class ServiceManager extends BaseManager {
	private static ServiceManager mInstance;

	private ServiceManager(InfoCommApp app) {
		super(app);
		initManager();
	}

	@Override
	protected void initManager() {
		// TODO Auto-generated method stub
	}

	@Override
	public void DestroyManager() {
		// TODO Auto-generated method stub

	}

	public static ServiceManager getInstance() {
		ServiceManager instance;
		if (mInstance == null) {
			synchronized (ServiceManager.class) {
				if (mInstance == null) {
					instance = new ServiceManager(InfoCommApp.getApplication());
					mInstance = instance;
				}
			}
		}
		return mInstance;
	}

	public static void setNullInstance() {
		mInstance = null;
	}

	// 注册
	public void regsiterUserId(String id) {
		String msg = null;
		try {
			msg = ProtocolDataOutput.registerUserIdToJSON(Utf8Code
					.utf8Encode(id));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (msg == null) {
			return;
		}

		RegisterTask mTask = new RegisterTask();
		mTask.execute("http://infocomm.duapp.com/register.py", msg);
	}

	// 登陆
	public void loginUserId(String id) {
		String msg = null;
		try {
			msg = ProtocolDataOutput.loginUserIdToJSON(Utf8Code.utf8Encode(id));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (msg == null) {
			return;
		}

		LoginTask mTask = new LoginTask();
		mTask.execute("http://infocomm.duapp.com/login.py", msg);
	}

	// 登陆
	public void setNickName(String id, String nick) {
		String msg = null;
		try {
			msg = ProtocolDataOutput.SetNickNameToJSON(Utf8Code.utf8Encode(id),
					Utf8Code.utf8Encode(nick));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (msg == null) {
			return;
		}

		SetNickNameTask mTask = new SetNickNameTask();
		mTask.execute("http://infocomm.duapp.com/setnickname.py", msg);
	}

	// 发送通知消息
	public String sendPushNotice(ArrayList<String> ids, String title,
			String message, String place, String link, String time) {
		String msg = null;
		try {
			msg = ProtocolDataOutput
					.sendPushNoticeToJSON(ids, Utf8Code.utf8Encode(title),
							Utf8Code.utf8Encode(message),
							Utf8Code.utf8Encode(place),
							Utf8Code.utf8Encode(link), time);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (msg == null) {
			return null;
		}

		SendPushNoticeTask mTask = new SendPushNoticeTask();
		mTask.execute("http://infocomm.duapp.com/sendpushnotice.py", msg);
		return msg;
	}

	// 发送消息
	public String sendPushMessage(String sender_id, String sender_nick,
			String receiver_id, String message) {
		String msg = null;
		try {
			msg = ProtocolDataOutput.sendPushMessageToJSON(
					Utf8Code.utf8Encode(sender_id),
					Utf8Code.utf8Encode(sender_nick),
					Utf8Code.utf8Encode(receiver_id),
					Utf8Code.utf8Encode(message));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (msg == null) {
			return null;
		}

		SendPushMessageTask mTask = new SendPushMessageTask();
		mTask.execute("http://infocomm.duapp.com/sendpushmessage.py", msg);
		return msg;
	}

	// 获取接收者列表
	public void getReceiverList(String flag) {
		String msg = null;
		try {
			msg = ProtocolDataOutput.getReceiverListToJSON(Utf8Code
					.utf8Encode(flag));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (msg == null) {
			return;
		}

		GetReceiverListTask mTask = new GetReceiverListTask();
		mTask.execute("http://infocomm.duapp.com/getreceiverlist.py", msg);
	}
}
