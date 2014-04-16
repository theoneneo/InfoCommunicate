package com.neo.infocommunicate.controller;

import java.util.ArrayList;

import com.neo.infocommunicate.InfoCommApp;

/**
 * @author LiuBing
 * @version 2014-3-6 下午4:33:24
 */
public class PersonManager extends BaseManager{
	private static PersonManager mInstance;
	private static ArrayList<String> receiver_list = new ArrayList<String>();
	private String user_id;
	
	private PersonManager(InfoCommApp app) {
		super(app);
	}
	
	@Override
	protected void initManager() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void DestroyManager() {
		// TODO Auto-generated method stub
		
	}

	public static PersonManager getInstance() {
		synchronized (PersonManager.class) {
			if (mInstance == null) {
				mInstance = new PersonManager(InfoCommApp.getApplication());
			}
			return mInstance;
		}
	}
	
	public ArrayList<String> getReceiverList(){
		return receiver_list;
	}
}
