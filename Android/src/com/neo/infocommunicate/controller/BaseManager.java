package com.neo.infocommunicate.controller;

import com.neo.infocommunicate.InfoCommApp;

import android.content.Context;

/**
 * @author LiuBing
 * @version 2014-2-17 下午2:36:04
 */
public abstract class BaseManager {
	protected InfoCommApp mApp;
	protected Context mContext;

	protected BaseManager(InfoCommApp app) {
		mApp = app;
		mContext = app.getApplicationContext();
	}

	protected abstract void initManager();

	protected abstract void DestroyManager();
}
