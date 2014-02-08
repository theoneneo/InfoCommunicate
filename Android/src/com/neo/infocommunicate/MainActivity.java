package com.neo.infocommunicate;

import com.neo.infocommunicate.push.PushMessageManager;

import android.os.Bundle;
import android.app.Activity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	init();
    }

    private void init(){
	PushMessageManager.startPush();
    }

}
