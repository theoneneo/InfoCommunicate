package com.neo.infocommunicate;

import com.baidu.frontia.FrontiaApplication;
import com.neo.infocommunicate.push.PushMessageManager;

public class InfoCommApp extends FrontiaApplication {
    private static InfoCommApp app;

    @Override
    public void onCreate() {
	super.onCreate();
	app = this;
	FrontiaApplication.initFrontiaApplication(this.getApplicationContext());
	PushMessageManager.getInstance();
    }

    public static InfoCommApp getApplication() {
	return app;
    }
}
