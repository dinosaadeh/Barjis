package com.smb215team.barjis;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.smb215team.barjis.facebook.FacebookServiceAndroid;

public class AndroidLauncher extends AndroidApplication {

	FacebookServiceAndroid facebookService;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		facebookService = new FacebookServiceAndroid(this);
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(getApplicationContext());
		AppEventsLogger.activateApp(this.getApplication());
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Barjis(facebookService), config);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		facebookService.callbackManager.onActivityResult(requestCode, resultCode, data);
	}
}
