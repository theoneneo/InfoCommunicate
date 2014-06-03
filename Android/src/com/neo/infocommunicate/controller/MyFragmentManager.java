package com.neo.infocommunicate.controller;

import java.util.ArrayList;

import com.neo.infocommunicate.InfoCommApp;
import com.neo.infocommunicate.MainActivity;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;

/**
 * @author LiuBing
 * @version 2014-3-14 下午3:48:08
 */
public class MyFragmentManager extends BaseManager implements
		OnBackStackChangedListener {
	private static MyFragmentManager mInstance;
	private ArrayList<String> mFragmentFlagList = new ArrayList<String>();
	private MainActivity mActivity;
	private String mPreviousFragmentFlag = null;// 调用backfragment接口后退，获取调用时的framgnet
	// flag

	public static final String FLAG = "flag";
	public static final String PROCESS_MAIN = "process_main";

	public static final String FRAGMENT_USER_LIST = "fragment_user_list";
	public static final String FRAGMENT_EDIT_NOTICE = "fragment_edit_notice";
	public static final String FRAGMENT_EDIT_MESSAGE = "fragment_edit_message";
	public static final String FRAGMENT_SELECT_FRIEND = "fragment_select_friend";

	private MyFragmentManager(InfoCommApp app) {
		super(app);
		// TODO Auto-generated constructor stub
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
	
	public static MyFragmentManager getInstance() {
		MyFragmentManager instance;
		if (mInstance == null) {
			synchronized (MyFragmentManager.class) {
				if (mInstance == null) {
					instance = new MyFragmentManager(InfoCommApp.getApplication());
					mInstance = instance;
				}
			}
		}
		return mInstance;
	}

	public static void setNullInstance() {
		mInstance = null;
	}

	public void setMainActivity(MainActivity activity) {
		mActivity = activity;
	}

	public ArrayList<String> getFragmentFlagList() {
		if (mFragmentFlagList == null)
			mFragmentFlagList = new ArrayList<String>();
		return mFragmentFlagList;
	}

	/**
	 * 添加fragment
	 * 
	 * @param id
	 *            容器id
	 * @param fragment
	 * @param flag
	 * @author yi.kang
	 * @date 2014-04-03
	 */
	public void addFragment(int id, Fragment fragment, String process_flag,
			String fragment_flag) {
		android.support.v4.app.FragmentManager fragmentManager = mActivity
				.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		String flag = process_flag + "-" + fragment_flag;
		Bundle b = new Bundle();
		b.putString(FLAG, flag);
		fragment.setArguments(b);
		fragmentTransaction.add(id, fragment, flag);
		fragmentTransaction.addToBackStack(flag);
		fragmentTransaction.commitAllowingStateLoss();
		fragmentManager.executePendingTransactions();
		fragmentManager.addOnBackStackChangedListener(this);// TODO 需要每次都调用吗？
	}
	
	public void addFragment(int id, Fragment fragment, String process_flag,
			String fragment_flag, Bundle b) {
		android.support.v4.app.FragmentManager fragmentManager = mActivity
				.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		String flag = process_flag + "-" + fragment_flag;
		b.putString(FLAG, flag);
		fragment.setArguments(b);
		fragmentTransaction.add(id, fragment, flag);
		fragmentTransaction.addToBackStack(flag);
		fragmentTransaction.commitAllowingStateLoss();
		fragmentManager.executePendingTransactions();
		fragmentManager.addOnBackStackChangedListener(this);// TODO 需要每次都调用吗？
	}

	// 替换fragment add child id, fragment， 不需要参数， 无动画
	public void addChildFragment(int parent_id, Fragment fragment,
			FragmentManager childFragmentManager) {
		FragmentTransaction fragmentTransaction = childFragmentManager
				.beginTransaction();
		fragmentTransaction.add(parent_id, fragment);
		fragmentTransaction.commitAllowingStateLoss();
		childFragmentManager.executePendingTransactions();
	}

	// 替换fragment add child id, fragment， 不需要参数， 有动画
	public void addChildFragment(int parent_id, Fragment fragment,
			FragmentManager childFragmentManager, int[] animators) {
		FragmentTransaction fragmentTransaction = childFragmentManager
				.beginTransaction();
		if (animators.length == 2) {
			fragmentTransaction.setCustomAnimations(animators[0], animators[1]);
		} else {
			fragmentTransaction.setCustomAnimations(animators[0], animators[1],
					animators[2], animators[3]);
		}
		fragmentTransaction.add(parent_id, fragment);
		fragmentTransaction.commitAllowingStateLoss();
		childFragmentManager.executePendingTransactions();
	}

	// 替换fragment replace id, fragment， 不需要参数， 无动画
	public void replaceFragment(int parent_id, Fragment fragment,
			String process_flag, String fragment_flag) {
		android.support.v4.app.FragmentManager fragmentManager = mActivity
				.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		String flag = process_flag + "-" + fragment_flag;
		Bundle b = new Bundle();
		b.putString(FLAG, flag);
		fragment.setArguments(b);
		fragmentTransaction.replace(parent_id, fragment, flag);
		fragmentTransaction.addToBackStack(flag);
		fragmentTransaction.commitAllowingStateLoss();
		fragmentManager.executePendingTransactions();
		fragmentManager.addOnBackStackChangedListener(this);// TODO 需要每次都调用吗？
	}

	// 替换fragment replace id, fragment， 需要参数， 无动画
	public void replaceFragment(int parent_id, Fragment fragment,
			String process_flag, String fragment_flag, Bundle b) {
		android.support.v4.app.FragmentManager fragmentManager = mActivity
				.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		String flag = process_flag + "-" + fragment_flag;
		b.putString(FLAG, flag);
		fragment.setArguments(b);
		fragmentTransaction.replace(parent_id, fragment, flag);
		fragmentTransaction.addToBackStack(flag);
		fragmentTransaction.commit();
		fragmentManager.addOnBackStackChangedListener(this);
	}

	// 替换fragment replace id, fragment， 不需要参数， 有动画
	public void replaceFragment(int parent_id, Fragment fragment,
			String process_flag, String fragment_flag, int[] animators) {
		android.support.v4.app.FragmentManager fragmentManager = mActivity
				.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		String flag = process_flag + "-" + fragment_flag;
		Bundle b = new Bundle();
		b.putString(FLAG, flag);
		fragment.setArguments(b);
		if (animators.length == 2) {
			fragmentTransaction.setCustomAnimations(animators[0], animators[1]);
		} else {
			fragmentTransaction.setCustomAnimations(animators[0], animators[1],
					animators[2], animators[3]);
		}
		fragmentTransaction.replace(parent_id, fragment, flag);
		fragmentTransaction.addToBackStack(flag);
		fragmentTransaction.commitAllowingStateLoss();
		fragmentManager.executePendingTransactions();
		fragmentManager.addOnBackStackChangedListener(this);
	}

	// 替换fragment replace id, fragment， 需要参数， 有动画
	public void replaceFragment(int parent_id, Fragment fragment,
			String process_flag, String fragment_flag, Bundle b, int[] animators) {
		android.support.v4.app.FragmentManager fragmentManager = mActivity
				.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		String flag = process_flag + "-" + fragment_flag;
		b.putString(FLAG, flag);
		fragment.setArguments(b);
		if (animators.length == 2) {
			fragmentTransaction.setCustomAnimations(animators[0], animators[1]);
		} else {
			fragmentTransaction.setCustomAnimations(animators[0], animators[1],
					animators[2], animators[3]);
		}
		fragmentTransaction.replace(parent_id, fragment, flag);
		fragmentTransaction.addToBackStack(flag);
		fragmentTransaction.commitAllowingStateLoss();
		fragmentManager.executePendingTransactions();
		fragmentManager.addOnBackStackChangedListener(this);
	}

	public void showFragmentDialog(DialogFragment fragment,
			String process_flag, String fragment_flag) {
		android.support.v4.app.FragmentManager fragmentManager = mActivity
				.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		String flag = process_flag + "-" + fragment_flag;
		Bundle b = new Bundle();
		b.putString(FLAG, flag);
		fragment.setArguments(b);
		fragmentTransaction.setCustomAnimations(0, 0,
				FragmentTransaction.TRANSIT_NONE,
				FragmentTransaction.TRANSIT_NONE);
		fragment.show(fragmentTransaction, flag);
	}

	// 后退到最近的一个fragment
	public void backFragment() {
		android.support.v4.app.FragmentManager fragmentManager = mActivity
				.getSupportFragmentManager();
		mPreviousFragmentFlag = fragmentManager.getBackStackEntryAt(
				fragmentManager.getBackStackEntryCount() - 1).getName();
		fragmentManager.popBackStackImmediate();
	}

	// 弹出指定flag所有的fragment
	public void backFragmentDownFlag(String flag) {
		android.support.v4.app.FragmentManager fragmentManager = mActivity
				.getSupportFragmentManager();
		mPreviousFragmentFlag = fragmentManager.getBackStackEntryAt(
				fragmentManager.getBackStackEntryCount() - 1).getName();
		fragmentManager
				.popBackStackImmediate(
						flag,
						android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

	// 弹出指定flag之上所有的fragment
	public void backFragmentUpFlag(String flag) {
		android.support.v4.app.FragmentManager fragmentManager = mActivity
				.getSupportFragmentManager();
		mPreviousFragmentFlag = fragmentManager.getBackStackEntryAt(
				fragmentManager.getBackStackEntryCount() - 1).getName();
		fragmentManager.popBackStackImmediate(flag, 0);
	}

	// 弹出所有的fragment
	public void backFragmentAll() {
		android.support.v4.app.FragmentManager fragmentManager = mActivity
				.getSupportFragmentManager();
		fragmentManager
				.popBackStackImmediate(
						null,
						android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

	public Fragment getFragment(String flag) {
		android.support.v4.app.FragmentManager fragmentManager = mActivity
				.getSupportFragmentManager();
		Fragment fragment = fragmentManager.findFragmentByTag(flag);
		return fragment;
	}

	// 后退管理刷新
	@Override
	public void onBackStackChanged() {
		// TODO Auto-generated method stub
		if (mFragmentFlagList.size() == 0)
			return;

		// if(mPreviousFragmentFlag.equals("XXX") &&
		// mFragmentFlagList.get(mFragmentFlagList.size() - 1).equals("XXX")){
		//
		// }
		//
		// if(mFragmentFlagList.get(mFragmentFlagList.size() -
		// 1).equals("XXX")){
		//
		// }
	}
}
