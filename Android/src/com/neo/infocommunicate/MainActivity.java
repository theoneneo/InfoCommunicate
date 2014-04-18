package com.neo.infocommunicate;

import com.neo.infocommunicate.controller.MyFragmentManager;
import com.neo.infocommunicate.controller.ServiceManager;
import com.neo.infocommunicate.fragment.MessageListFragment;
import com.neo.infocommunicate.fragment.UserListFragment;
import com.neo.infocommunicate.listener.ServiceListener;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity implements ServiceListener {
	private static final String[] CONTENT = new String[] { "信息通" };
	private static final int[] ICONS = new int[] { R.drawable.ic_launcher };

	private MessageListFragment messageListFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MyFragmentManager.getInstance().setMapActivity(this);
		init();
	}
//TODO android:targetSdkVersion="10" 设定成17
	protected void onDestroy() {
		ServiceManager.getInstance().getServiceListenerAbility()
				.removeListener(this);
		super.onDestroy();
	}

	private void init() {
		ServiceManager.getInstance().getServiceListenerAbility()
				.addListener(this);
		initUI();
		initData();
	}

	private void initUI() {
		FragmentPagerAdapter adapter = new MainAdapter(
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
	public void onRegister(String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLogin(String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPushMessage(String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetReceiverList() {
		// TODO Auto-generated method stub
		MyFragmentManager.getInstance().replaceFragment(R.id.content_frame,
				new UserListFragment(), MyFragmentManager.PROCESS_PERSONINFO,
				MyFragmentManager.FRAGMENT_MINE_MAIN);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 1, 1, "关于路佳");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == 1) {
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
}
