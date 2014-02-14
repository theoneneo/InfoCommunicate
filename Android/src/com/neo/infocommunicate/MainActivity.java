package com.neo.infocommunicate;

import com.neo.infocommunicate.push.PushMessageManager;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.app.Activity;

public class MainActivity extends Activity {
    private static final String[] CONTENT = new String[] { "提醒", "我的地盘" };
    private static final int[] ICONS = new int[] { R.drawable.ic_launcher,
	    R.drawable.ic_launcher, };

//    private static RemindListFragment remindList;
//    private static MessageListFragment mysiteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	init();
    }

    private void init() {
	PushMessageManager.startPush();
	initUI();
    }

    private void initUI() {
	FragmentPagerAdapter adapter = new MainAdapter(
		getSupportFragmentManager());
	ViewPager pager = (ViewPager) findViewById(R.id.pager);
	pager.setAdapter(adapter);
	TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
	indicator.setViewPager(pager);
    }

    class MainAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
	public MainAdapter(FragmentManager fm) {
	    super(fm);
	}

	@Override
	public Fragment getItem(int position) {
	    if (position == 0) {
		if (remindList == null)
		    remindList = new RemindListFragment();
		return remindList;
	    } else if (position == 1) {
		if (mysiteList == null)
		    mysiteList = new MessageListFragment();
		return mysiteList;
	    }
	    return remindList;
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
