package com.neo.infocommunicate.fragment;

import com.neo.infocommunicate.controller.MyFragmentManager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;

public abstract class BaseListFragment extends ListFragment {

	static final String FLAG = "flag";
	protected Context mContext;

	/**
	 * 获取Fragment的标记字符串,每个Fragment都是唯一的。 暂时不用。
	 */
	public String getFlagStr() {
		return "";
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity().getApplicationContext();

		Bundle args = getArguments();
		if (args != null) {
			MyFragmentManager.getFragmentFlagList().add(args.getString(FLAG));
		}
	}

	@Override
	public void onDestroy() {
		if (MyFragmentManager.getFragmentFlagList().size() > 0)
			MyFragmentManager.getFragmentFlagList().remove(
					MyFragmentManager.getFragmentFlagList().size() - 1);
		super.onDestroy();
	}

	/** Fragment在结束（隐藏）的时候向调用者回传数据 */
	public interface OnFinishListener {
		/**
		 * 回传数据
		 * 
		 * @param flag
		 *            结束的Fragment的flag
		 * @param data
		 *            数据包
		 */
		public void onFinish(String flag, Bundle data);
	}

	private OnFinishListener mFinishListener;

	/** 设置监听回调 */
	public void setOnFinishListener(OnFinishListener l) {
		mFinishListener = l;
	}

	/**
	 * 供子类调用，用以回传数据
	 * 
	 * @param data
	 *            要回传的数据包
	 */
	protected void callOnFinish(Bundle data) {
		if (mFinishListener != null) {
			mFinishListener.onFinish(getFlagStr(), data);
		}
	}
}
