package com.neo.infocommunicate.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.neo.infocommunicate.R;
import com.neo.infocommunicate.controller.MyFragmentManager;

/**
 * @author kunge.hu
 * 
 * */
public abstract class BaseDialogFragment extends DialogFragment {

	private static String FLAG = "flag";
	protected Context mContext;

	/**
	 * 获取Fragment的标记字符串,每个Fragment都是唯一的。 暂时不用。
	 */
	public String getFlagStr() {
		return FLAG;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 透明
		setStyle(DialogFragment.STYLE_NORMAL,
				R.style.dialogfragment_transparent_bg);
		mContext = getActivity().getApplicationContext();

		Bundle args = getArguments();
		if (args != null) {
			MyFragmentManager.getInstance().getFragmentFlagList()
					.add(args.getString(FLAG));
			FLAG = args.getString(FLAG);
		}
	}

	@Override
	public void onDestroy() {
		try {
			MyFragmentManager
					.getInstance()
					.getFragmentFlagList()
					.remove(MyFragmentManager.getInstance()
							.getFragmentFlagList().size() - 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
