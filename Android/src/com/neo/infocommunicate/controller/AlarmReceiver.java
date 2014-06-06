package com.neo.infocommunicate.controller;

import com.neo.infocommunicate.MainActivity;
import com.neo.infocommunicate.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
	public void onReceive(Context context, Intent intent) {
		String key = intent.getStringExtra("key");
		if (key == null)
			return;

		for (int i = 0; i < MessageManager.getInstance().getNoticeInfos()
				.size(); i++) {
			if (key.equals(MessageManager.getInstance().getNoticeInfos().get(i).key)) {
				startNoticeNotify(
						context,
						"notice",
						MessageManager.getInstance().getNoticeInfos().get(i).key,"信息通提醒", "会议时间快到了！");
			}
		}
	}

	private void startNoticeNotify(Context context, String fragment,
			String key, String title, String message) {
		Intent intent = new Intent(context, MainActivity.class);
		intent.putExtra("fragment", fragment);
		intent.putExtra("key", key);
		PendingIntent pd = PendingIntent.getActivity(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		Notification builder = new Notification();
		builder.icon = R.drawable.ic_launcher;
		builder.tickerText = title;
		builder.flags = Notification.FLAG_AUTO_CANCEL;
		builder.defaults = Notification.DEFAULT_ALL;
		builder.setLatestEventInfo(context, title, message, pd);
		NotificationManager nm = (NotificationManager) context
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		nm.notify(0, builder);
	}
}