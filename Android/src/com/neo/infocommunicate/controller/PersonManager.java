package com.neo.infocommunicate.controller;

import com.neo.infocommunicate.InfoCommApp;

/**
 * @author LiuBing
 * @version 2014-3-6 下午4:33:24
 */
public class PersonManager {
    private InfoCommApp mApp;
    private static PersonManager mInstance;
    public String channel_id, user_id;

    private PersonManager(InfoCommApp app) {
	mApp = app;
    }

    public static PersonManager getInstance() {
	synchronized (PersonManager.class) {
	    if (mInstance == null) {
		mInstance = new PersonManager(InfoCommApp.getApplication());
	    }
	    return mInstance;
	}
    }
}
