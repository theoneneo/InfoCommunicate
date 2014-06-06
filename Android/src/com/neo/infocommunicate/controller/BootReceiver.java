package com.neo.infocommunicate.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
			// 重新计算闹铃时间，并调第一步的方法设置闹铃时间及闹铃间隔时间
		}
	}
}
