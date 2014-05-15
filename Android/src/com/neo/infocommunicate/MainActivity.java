package com.neo.infocommunicate;

import com.neo.infocommunicate.controller.MessageManager;
import com.neo.infocommunicate.controller.MyFragmentManager;
import com.neo.infocommunicate.controller.ServiceManager;
import com.neo.infocommunicate.event.BroadCastEvent;
import com.neo.infocommunicate.event.ServiceEvent;
import com.neo.infocommunicate.fragment.EditMessageFragment;
import com.neo.infocommunicate.fragment.MessageListFragment;
import com.neo.tools.RingTong;
import com.tencent.android.tpush.XGPushTextMessage;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

import de.greenrobot.event.EventBus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity {
	private static final String[] CONTENT = new String[] { "信息通" };
	private static final int[] ICONS = new int[] { R.drawable.ic_launcher };

	private FragmentPagerAdapter adapter;
	private MessageListFragment messageListFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		EventBus.getDefault().register(this, ServiceEvent.class,
				XGPushTextMessage.class, BroadCastEvent.class);
		MyFragmentManager.getInstance().setMapActivity(this);
		init();
	}

	// TODO android:targetSdkVersion="10" 设定成17
	protected void onDestroy() {
//		EventBus.getDefault().unregister(this, ServiceEvent.class,
//				XGPushTextMessage.class, BroadCastEvent.class);
		super.onDestroy();
	}

	private void init() {
		initUI();
		initData();
	}

	private void initUI() {
		adapter = new MainAdapter(
				getSupportFragmentManager());
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);
		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);
	}

	private void initData() {
		ServiceManager.getInstance().getReceiverList("all");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 1, 1, "发送");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == 1) {
			MyFragmentManager.getInstance().replaceFragment(R.id.content_frame,
					new EditMessageFragment(), MyFragmentManager.PROCESS_MAIN,
					MyFragmentManager.FRAGMENT_EDIT_MESSAGE);
		}
		return true;
	}

	class MainAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
		public MainAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0) {
				if (messageListFragment == null)
					messageListFragment = new MessageListFragment();
				return messageListFragment;
			}
			return messageListFragment;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position % CONTENT.length].toUpperCase();
		}

		@Override
		public int getIconResId(int index) {
			return ICONS[index];
		}

		@Override
		public int getCount() {
			return CONTENT.length;
		}
	}

	public void onEventMainThread(ServiceEvent event) {
		switch (event.getType()) {
		case ServiceEvent.SERVICE_GET_USERID_EVENT:
			break;
		default:
			break;
		}
	}

	public void onEventMainThread(XGPushTextMessage message) {
		String customContent = message.getCustomContent();
		if (customContent != null && customContent.length() != 0) {
			MessageManager.getInstance().addMessageInfo(customContent);
		}
		((MessageListFragment) adapter.getItem(0)).updateAdapter();
		RingTong.systemNotificationRing(getApplicationContext(), null);
	}

	public void onEventMainThread(BroadCastEvent event) {
		switch (event.getType()) {
		case BroadCastEvent.LOAD_MESSAGE_EVENT:
			break;
		default:
			break;
		}
	}
}
