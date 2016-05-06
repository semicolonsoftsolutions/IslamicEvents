package com.example.nonameproject;

import java.io.IOException;

import com.example.nonameproject.util.AppPrefs;
import com.example.nonameproject.util.GCMConfig;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class ActivityNotifications extends ActivityMaster {
	
	private static final int CODE_REGISTER_DEVICE = 1;
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notifications);
		context = this;
		
		/***
		 * Check to see if user opens this activity by clicking on notification
		 * from notification bar, if it's the case then reset notification count
		 * variable in System class.
		 */
		Intent intent = getIntent();
		if (intent.hasExtra("NOTIFICATION_CLICKED") && intent.getBooleanExtra("NOTIFICATION_CLICKED", false)){
			ApplicationClass.mNotificationCount = 0;
		}
		
		/**CHECK IF THE DEVICE HAS REGISTRATION ID, IF IT DOES THEN IT IS REGISTERED**/
		String regID = new AppPrefs(ActivityNotifications.this).get(AppPrefs.REG_ID);
		if (regID == null) {
			/**TAKE USER TO REGISTER ACTIVITY TO REGISTER DEVICE ON CLOUD**/
			Intent registerActivityIntent = new Intent(ActivityNotifications.this,ActivityRegister.class);
			startActivityForResult(registerActivityIntent, CODE_REGISTER_DEVICE);
			
			return;
		}
		
		
		Log.d("NONAME", regID);
		
		
		/**CHECK IF DEVICE NEED RE REGISTRATION
		 * COMPARE THE NEW APP VERSION WITH CURRENT VERSION
		 * TO DETERMINE IF THE USER HAS INSTALLED AN UPDATE FROM
		 * GOOGLE PLAY OR NOT, IF USER HAS UPDATED THE APP FROM 
		 * GOOGLE PLAY THEN RE REGISTER DEVICE AND UPDATE ON THE SERVER**/
		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case CODE_REGISTER_DEVICE:
			displayMessage("You are registered!");
			break;
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_notifications, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
}
