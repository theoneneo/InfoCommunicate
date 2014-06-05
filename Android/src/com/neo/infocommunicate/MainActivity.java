package com.neo.infocommunicate;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.WindowManager.LayoutParams;

import com.neo.infocommunicate.controller.MessageManager;
import com.neo.infocommunicate.controller.MyFragmentManager;
import com.neo.infocommunicate.controller.ServiceManager;
import com.neo.infocommunicate.event.BroadCastEvent;
import com.neo.infocommunicate.event.ServiceEvent;
import com.neo.infocommunicate.fragment.ChatRoomFragment;
import com.neo.infocommunicate.fragment.MessageListFragment;
import com.neo.infocommunicate.fragment.NotificationListFragment;
import com.neo.infocommunicate.fragment.UserListFragment;
import com.neo.tools.RingTong;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

import de.greenrobot.event.EventBus;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity {
	private static final String[] CONTENT = new String[] { "会议通知", "信息", "联系人" };

	private FragmentPagerAdapter adapter;
	private NotificationListFragment notificListFragment;
	private MessageListFragment messageListFragment;
	private UserListFragment userListFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);
		EventBus.getDefault().register(this, ServiceEvent.class,
				BroadCastEvent.class);
		MyFragmentManager.getInstance().setMainActivity(this);
		init();
	}

	protected void onDestroy() {
		EventBus.getDefault().unregister(this, ServiceEvent.class,
				BroadCastEvent.class);
		super.onDestroy();
	}

	private void init() {
		initUI();
		initData();
	}

	private void initUI() {
		notificListFragment = new NotificationListFragment();
		messageListFragment = new MessageListFragment();
		userListFragment = new UserListFragment();
		adapter = new MainAdapter(getSupportFragmentManager());
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);
		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);
	}

	private void initData() {
		String fragment = getIntent().getStringExtra("fragment");
		if (fragment == null) {
			ServiceManager.getInstance().getReceiverList("all");
		} else {
			if ("notice".equals(fragment)) {
				getIntent().getStringExtra("key");
				notificListFragment.updateAdapter();
			} else if ("message".equals(fragment)) {
				Bundle b = new Bundle();
				b.putString("sender_id", getIntent().getStringExtra("key"));
				MyFragmentManager.getInstance().replaceFragment(
						R.id.content_frame, new ChatRoomFragment(),
						MyFragmentManager.PROCESS_MAIN,
						MyFragmentManager.FRAGMENT_EDIT_MESSAGE, b);
				messageListFragment.updateAdapter();
			}
		}
	}

	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		String fragment = intent.getStringExtra("fragment");
		if (fragment == null) {
			return;
		} else {
			if ("notice".equals(fragment)) {
				getIntent().getStringExtra("key");
				notificListFragment.updateAdapter();
			} else if ("message".equals(fragment)) {
				MyFragmentManager.getInstance().backFragmentAll();
				Bundle b = new Bundle();
				b.putString("sender_id", intent.getStringExtra("key"));
				MyFragmentManager.getInstance().replaceFragment(
						R.id.content_frame, new ChatRoomFragment(),
						MyFragmentManager.PROCESS_MAIN,
						MyFragmentManager.FRAGMENT_EDIT_MESSAGE, b);
				messageListFragment.updateAdapter();
			}
		}
	}

	public void onEventMainThread(ServiceEvent event) {
		switch (event.getType()) {
		case ServiceEvent.SERVICE_GET_USERID_EVENT:
			userListFragment.updateAdapter();
			break;
		default:
			break;
		}
	}

	public void onEventMainThread(BroadCastEvent event) {
		switch (event.getType()) {
		case BroadCastEvent.NEW_NOTICE_EVENT:
			notificListFragment.updateAdapter();
			RingTong.systemNotificationRing(this, null);
			break;
		case BroadCastEvent.NEW_MESSAGE_EVENT:
			messageListFragment.updateAdapter();
			if (MessageManager.getInstance().mCurChatRoom != null) {
				if ((event.getObject() != null)
						&& !((String) event.getObject()).equals(MessageManager
								.getInstance().mCurChatRoom.sender_id)) {
					RingTong.systemNotificationRing(this, null);
				}
			} else {
				RingTong.systemNotificationRing(this, null);
			}
			break;
		case BroadCastEvent.CHANGE_PROMPT_EVENT:
		case BroadCastEvent.LOAD_NOTICE_EVENT:
			notificListFragment.updateAdapter();
			break;
		default:
			break;
		}
	}

	class MainAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
		public MainAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0) {
				if (notificListFragment == null)
					notificListFragment = new NotificationListFragment();
				return notificListFragment;
			} else if (position == 1) {
				if (messageListFragment == null)
					messageListFragment = new MessageListFragment();
				return messageListFragment;
			} else if (position == 2) {
				if (userListFragment == null)
					userListFragment = new UserListFragment();
				return userListFragment;
			}
			return notificListFragment;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position % CONTENT.length].toUpperCase();
		}

		@Override
		public int getIconResId(int index) {
			return 0;
		}

		@Override
		public int getCount() {
			return CONTENT.length;
		}
	}
}
