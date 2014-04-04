package com.neo.infocommunicate.controller;

import org.json.JSONException;

import com.neo.infocommunicate.InfoCommApp;
import com.neo.infocommunicate.protocol.ProtocolDataOutput;
import com.neo.infocommunicate.task.PostJsonTask;
import com.neo.infocommunicate.task.SendPushMessageTask;

/**
 * @author LiuBing
 * @version 2014-3-6 下午4:33:24
 */
public class ServiceManager {
    private InfoCommApp mApp;
    private static ServiceManager mInstance;

    private ServiceManager(InfoCommApp app) {
    	mApp = app;
    }

    public static ServiceManager getInstance() {
		synchronized (ServiceManager.class) {
		    if (mInstance == null) {
			mInstance = new ServiceManager(InfoCommApp.getApplication());
		    }
		    return mInstance;
		}
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

		PostJsonTask mTask = new PostJsonTask();
		mTask.execute("http://infocomm.duapp.com/sendpushmessage.py", msg);    	
    }
}
