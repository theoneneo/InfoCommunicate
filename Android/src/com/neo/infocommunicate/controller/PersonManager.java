package com.neo.infocommunicate.controller;

import java.util.ArrayList;

import com.neo.infocommunicate.InfoCommApp;
import com.neo.infocommunicate.data.UserInfo;

/**
 * @author LiuBing
 * @version 2014-3-6 下午4:33:24
 */
public class PersonManager extends BaseManager{
	private static PersonManager mInstance;
	private static ArrayList<UserInfo> receiver_list = new ArrayList<UserInfo>();
	private static ArrayList<UserInfo> send_receiver_list = new ArrayList<UserInfo>();
	
	private PersonManager(InfoCommApp app) {
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

	public static PersonManager getInstance() {
		PersonManager instance;
		if (mInstance == null) {
			synchronized (PersonManager.class) {
				if (mInstance == null) {
					instance = new PersonManager(InfoCommApp.getApplication());
					mInstance = instance;
				}
			}
		}
		return mInstance;
	}

	public static void setNullInstance() {
		mInstance = null;
	}
	
	public ArrayList<UserInfo> getReceiverList(){
		return receiver_list;
	}
	
	public ArrayList<UserInfo> getSendReceiverList(){
		return send_receiver_list;
	}
}
