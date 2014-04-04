package com.neo.infocommunicate.controller;

import org.json.JSONException;

import com.neo.infocommunicate.InfoCommApp;
import com.neo.infocommunicate.listener.ServiceListenerAbility;
import com.neo.infocommunicate.protocol.ProtocolDataOutput;
import com.neo.infocommunicate.task.RegisterTask;
import com.neo.infocommunicate.task.SendPushMessageTask;

/**
 * @author LiuBing
 * @version 2014-3-6 下午4:33:24
 */
public class ServiceManager {
    private InfoCommApp mApp;
    private static ServiceManager mInstance;
    private static ServiceListenerAbility serviceListenerAbility;

    private ServiceManager(InfoCommApp app) {
    	mApp = app;
    	serviceListenerAbility = new ServiceListenerAbility();
    }

    public static ServiceManager getInstance() {
		synchronized (ServiceManager.class) {
		    if (mInstance == null) {
			mInstance = new ServiceManager(InfoCommApp.getApplication());
		    }
		    return mInstance;
		}
    }
    
	public ServiceListenerAbility getServiceListenerAbility() {
		return serviceListenerAbility;
	}
    
    public void saveUserId(String id){
		String msg = null;
		ProtocolDataOutput output = new ProtocolDataOutput();
		try {
			msg = output.saveUserIdToJSON(id);
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
    
	public void sendPushMessage(String[] ids, String title,
			String message, String place, String link, String time) {
		String msg = null;
		ProtocolDataOutput output = new ProtocolDataOutput();
		try {
			msg = output.sendPushMessageToJSON(ids, title, message, place,
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
}
