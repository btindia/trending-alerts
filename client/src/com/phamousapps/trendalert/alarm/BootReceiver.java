package com.phamousapps.trendalert.alarm;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		new AlarmHelper(context, AlarmManager.INTERVAL_HALF_HOUR);
	}
}
