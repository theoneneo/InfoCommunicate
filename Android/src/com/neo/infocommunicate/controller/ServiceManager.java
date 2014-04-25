package com.neo.infocommunicate.controller;

import java.util.ArrayList;

import org.json.JSONException;

import com.neo.infocommunicate.InfoCommApp;
import com.neo.infocommunicate.protocol.ProtocolDataOutput;
import com.neo.infocommunicate.task.GetReceiverListTask;
import com.neo.infocommunicate.task.LoginTask;
import com.neo.infocommunicate.task.RegisterTask;
import com.neo.infocommunicate.task.SendPushMessageTask;

/**
 * @author LiuBing
 * @version 2014-3-6 下午4:33:24
 */
public class ServiceManager extends BaseManager{
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
	protected void DestroyManager() {
		// TODO Auto-generated method stub
		
	}

    public static ServiceManager getInstance() {
		synchronized (ServiceManager.class) {
		    if (mInstance == null) {
			mInstance = new ServiceManager(InfoCommApp.getApplication());
		    }
		    return mInstance;
		}
    }
 
	//注册
    public void regsiterUserId(String id){
		String msg = null;
		try {
			msg = ProtocolDataOutput.registerUserIdToJSON(id);
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
    
	//登陆
    public void loginUserId(String id){
		String msg = null;
		try {
			msg = ProtocolDataOutput.loginUserIdToJSON(id);
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
    
    //发送消息
	public void sendPushMessage(ArrayList<String> ids, String title,
			String message, String place, String link, String time) {
		String msg = null;
		try {
			msg = ProtocolDataOutput.sendPushMessageToJSON(ids, title, message, place,
					link, time);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (msg == null) {
			return;
		}

		SendPushMessageTask mTask = new SendPushMessageTask();
		mTask.execute("http://infocomm.duapp.com/sendpushmessage.py", msg);
	}
	
	//获取接收者列表
	public void getReceiverList(String flag){
		String msg = null;
		try {
			msg = ProtocolDataOutput.getReceiverListToJSON(flag);
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
