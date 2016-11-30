package com.reciever;

import android.content.BroadcastReceiver; 
import android.content.Context;
import android.content.Intent;

public class PhoneReciever extends BroadcastReceiver {

	 private final String ACTION = "android.intent.action.BOOT_COMPLETED";
	 
	@Override
	public void onReceive(Context context, Intent intent) {
		
		// TODO Auto-generated method stub
		
		if (intent.getAction().equals(ACTION)) {
			
			Intent myintent = new Intent(context, PhoneService.class);  
	        myintent.setAction("com.reciever.PhoneReciever");  
	        context.startService(myintent);  
		}
		
		Intent myintent = new Intent(context, PhoneService.class);  
        myintent.setAction("com.reciever.PhoneReciever");  
        context.startService(myintent);  
	}
}
