package com.neo.infocommunicate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.neo.infocommunicate.controller.MyFragmentManager;
import com.neo.infocommunicate.controller.ServiceManager;
import com.neo.infocommunicate.event.BroadCastEvent;
import com.neo.infocommunicate.event.ServiceEvent;
import com.neo.infocommunicate.fragment.MessageListFragment;
import com.neo.infocommunicate.fragment.NotificationListFragment;
import com.neo.infocommunicate.fragment.UserListFragment;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

import de.greenrobot.event.EventBus;

public class MainActivity extends FragmentActivity {
	private static final String[] CONTENT = new String[] { "会议通知", "信息", "联系人" };

	private FragmentPagerAdapter adapter;
	private NotificationListFragment notificListFragment;
	private MessageListFragment messageListFragment;
	private UserListFragment userListFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		ServiceManager.getInstance().getReceiverList("all");
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
		case BroadCastEvent.NEW_MESSAGE_EVENT:
			((MessageListFragment)adapter.getItem(1)).updateAdapter();
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
