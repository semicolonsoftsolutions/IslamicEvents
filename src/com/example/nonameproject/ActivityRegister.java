package com.example.nonameproject;

import com.example.nonameproject.util.AppPrefs;
import com.example.nonameproject.util.DialogFactory;
import com.example.nonameproject.util.DialogType;
import com.example.nonameproject.util.GCMConfig;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ActivityRegister extends ActivityMaster {
	
	private static final int MAX_TRIES = 5;
	private Context context;
	private Button bRegisterUser;
	private EditText etPhoneNumber;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		context = this;
		
		bRegisterUser = (Button) findViewById(R.id.bRegisterUser);
		etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
		
		bRegisterUser.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String phoneNumber = etPhoneNumber.getText().toString();
				if (phoneNumber.length()<=0){
					displayMessage("Invalid phone number");
					return;
				}
				
				/**REGISTER IN BACKGROUND IF EVERYTING IS FINE, GCM WON'T ALLOW REGISTERING
				 * IN THE UI THREAD SO THAT'S WHY WE DO IT IN ASYNCH TASK INSTEAD. . . . . **/
				registerInBackground();
				
				
			}
		});
		
	}
	
	private void registerInBackground(){
		
		final ProgressDialog waitDialog = (ProgressDialog) DialogFactory.createDialog(DialogType.DIALOG_WAIT, context);
		waitDialog.setTitle("Please wait!");
		waitDialog.setMessage("Registering device");
		waitDialog.setIndeterminate(true);
		
		
		final AlertDialog errorDialog = (AlertDialog)DialogFactory.createDialog(DialogType.DIALOG_ERROR, context);
		errorDialog.setTitle("Error");
		errorDialog.setMessage("Check your internet connection and try again!");
		
		
		new AsyncTask<Void, Void, String>() {

			protected void onPreExecute() {
				waitDialog.show();
			};
			
			@Override
			protected String doInBackground(Void... params) {
				
				/**INITIATE ID VARIABLE FROM NULL*/
				String id = null;
				
				GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
				
				/** PROBLEM MIGHT OCCURE SO TRY MAX NUMBER OF TIMES MAX = 5 **/
				for (int count = 1; count <= MAX_TRIES; count++) {
					
					logMessage("Trying to register on cloud, try: "+count);
					try {
					
						id = gcm.register(GCMConfig.senderID(ActivityRegister.this));
						
						// WE HAVE A REGISTRATION ID, BREAK THE LOOP AND RETURN
						if (id != null) {break;}
						
					} catch (Exception e) {logMessage("Unable to register on cloud, error: "+e.getMessage());}
				}

				return id;
			}

			protected void onPostExecute(String regID) {
				if (!TextUtils.isEmpty(regID)) {
					AppPrefs prefs= new AppPrefs(ActivityRegister.this);
					prefs.save(AppPrefs.REG_ID, regID);
					waitDialog.cancel();
					setResult(RESULT_OK);
					finish();
				}else{
					waitDialog.cancel();
					errorDialog.show();
				}
			};
		}.execute();
	}
}