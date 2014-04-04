package com.neo.infocommunicate;

import com.neo.infocommunicate.controller.ServiceManager;
import com.neo.infocommunicate.fragment.MessageListFragment;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity {
	private static final String[] CONTENT = new String[] { "信息通" };
	private static final int[] ICONS = new int[] { R.drawable.ic_launcher };

	private MessageListFragment messageListFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	private void init() {
		initUI();
		initData();
		
		SharedPreferences mSharedPreferences = getSharedPreferences(
				"SharedPreferences", 0);
		boolean server_id = mSharedPreferences.getBoolean("server_id", false);
		if(!server_id){
			String user_id = mSharedPreferences.getString("user_id", null);
			if(user_id != null)
				ServiceManager.getInstance().saveUserId(user_id);
		}
	}

	private void initUI() {
		FragmentPagerAdapter adapter = new MainAdapter(
				getSupportFragmentManager());
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);
		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);
	}
	
	private void initData(){
		
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
