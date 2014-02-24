package com.neo.tools;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotificationUtil {
	public static void startAlertNotify(Context context, int iconId, String title,
			String tickerText, int defaults, String message) {
		SystemUtil.instance().wakeLockStart();
		Intent intent = new Intent();
		PendingIntent pd = PendingIntent.getActivity(context, 0, intent, 0);
		Notification baseNF = new Notification();
		baseNF.icon = iconId;
		baseNF.tickerText = title;
		baseNF.defaults = defaults;
		baseNF.setLatestEventInfo(context, title, message, pd);
		NotificationManager nm = (NotificationManager) context
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		nm.notify(0, baseNF);
		SystemUtil.instance().wakeLockStop();
	}
}
