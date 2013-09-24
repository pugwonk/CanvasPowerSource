package com.betaminus.phonepowersource;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class PowerSourceReceiver extends BroadcastReceiver {
	@Override
	public final void onReceive(Context context, Intent intent) {
        Log.d(PowerSourcePlugin.LOG_TAG, "Got power state");
		
        String action = intent.getAction();
        
        PowerSourcePlugin.stateChanged(action, context);
	}
}
