package com.neo.infocommunicate.controller;

import java.util.List;

import com.neo.infocommunicate.MainActivity;
import com.neo.infocommunicate.R;
import com.neo.infocommunicate.data.MessageInfo;
import com.neo.infocommunicate.data.NoticeInfo;
import com.neo.infocommunicate.event.BroadCastEvent;
import com.tencent.android.tpush.XGPushTextMessage;

import de.greenrobot.event.EventBus;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

public class MessageService extends Service {

	private IBinder mBinder = new MessageBinder();

	public MessageService() {
	}

	@Override
	public void onCreate() {
		super.onCreate();
		EventBus.getDefault().register(this, XGPushTextMessage.class);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this, XGPushTextMessage.class);
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public class MessageBinder extends Binder {
		public MessageService getService() {
			return MessageService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		if (null == mBinder)
			mBinder = new MessageBinder();
		return mBinder;
	}

	public void onEventMainThread(XGPushTextMessage message) {
		String customContent = message.getCustomContent();
		if (customContent != null && customContent.length() != 0) {
			String type = message.getContent();
			if (type == null)
				return;

			if (isForeground()) {
				if ("notice".equals(type)) {
					MessageManager.getInstance().addNoticeInfo(customContent);
					EventBus.getDefault().post(
							new BroadCastEvent(BroadCastEvent.NEW_NOTICE_EVENT,
									null));
				} else if ("message".equals(type)) {
					MessageManager.getInstance()
							.addMessageInfo(customContent);
					EventBus.getDefault().post(
							new BroadCastEvent(
									BroadCastEvent.NEW_MESSAGE_EVENT, null));
				}
			} else {
				if ("notice".equals(type)) {
					NoticeInfo notice = MessageManager.getInstance().addNoticeInfo(customContent);
					startNoticeNotify(this, "notice", notice.key, "信息通提醒", "您有新的会议通知！");
				} else if ("message".equals(type)) {
					MessageInfo msg = MessageManager.getInstance()
							.addMessageInfo(customContent);
					startNoticeNotify(this, "message", msg.sender_id, "信息通提醒", "您有新的消息！");
				}
			}
		}
	}

	private boolean isForeground() {
		ActivityManager am = (ActivityManager) this
				.getSystemService(ACTIVITY_SERVICE);
		if (am == null)
			return false;

		List<RunningTaskInfo> runningTaskInfos = am.getRunningTasks(1);
		if (runningTaskInfos == null || runningTaskInfos.size() == 0)
			return false;

		String topAppName = null;
		topAppName = runningTaskInfos.get(0).topActivity.getPackageName();
		if (topAppName == null)
			return false;
		if ("com.neo.infocommunicate".equals(topAppName))
			return true;

		return false;
	}

	private void startNoticeNotify(Context context, String fragment, String key,
			String title, String message) {
		Intent intent = new Intent(this, MainActivity.class);
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
