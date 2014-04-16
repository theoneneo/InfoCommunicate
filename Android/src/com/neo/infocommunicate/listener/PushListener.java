package com.neo.infocommunicate.listener;

import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

public interface PushListener extends Listener {
	public void onLogin(int errorCode, XGPushRegisterResult result);
	public void onUnLogin(int errorCode);
	public void onSetTagResult(int errorCode, String tag);
	public void onDeleteTagResult(int errorCode, String tag);
	public void onTextMessage(XGPushTextMessage msg);
	public void onNotifactionClickedResult(XGPushClickedResult message);
	public void onNotifactionShowedResult(XGPushShowedResult notifiShowedRlt);
}
