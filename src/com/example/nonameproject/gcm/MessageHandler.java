package com.example.nonameproject.gcm;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.nonameproject.ApplicationClass;
import com.example.nonameproject.util.System;

public class MessageHandler extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String message = intent.getStringExtra(ApplicationClass.KEY_EVENT_ID);
		Toast.makeText(context,message, Toast.LENGTH_LONG).show();
		System.getInstance(context).showNotification();
	}

}
