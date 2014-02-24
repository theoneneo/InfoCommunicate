package com.neo.tools;

import android.content.Context;
import android.media.MediaPlayer;
import android.provider.Settings;

public class RingTong {
	public static String musicUrl = null;
	public static MediaPlayer player;
	public static void systemNotificationRing(Context context, String musicUrl) {
		if (musicUrl == null) {
			musicUrl = Settings.System.getString(context.getContentResolver(),
					Settings.System.NOTIFICATION_SOUND);
		}
		player = new MediaPlayer();
		try {
			player.setDataSource(musicUrl);
			player.prepare();
		} catch (Exception e) {
			e.printStackTrace();
		}
		player.start();
	}
	
	public static void stopRing(){
		if(player != null)
			player.stop();
	}
}
