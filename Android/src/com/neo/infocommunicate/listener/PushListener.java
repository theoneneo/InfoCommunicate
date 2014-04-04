package com.neo.infocommunicate.listener;

import com.tencent.android.tpush.XGPushRegisterResult;

public interface PushListener extends Listener {
	public void onLogin(int errorCode, XGPushRegisterResult result);
}
