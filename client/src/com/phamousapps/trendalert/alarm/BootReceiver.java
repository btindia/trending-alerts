package com.phamousapps.trendalert.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.phamousapps.trendalert.utils.PrefsHelper;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		PrefsHelper ph = new PrefsHelper(context);
		AlarmHelper ah = new AlarmHelper(context);

		if (ph.isAlertsEnabled()) {
			ah.setAlarm(ph.getAlertsFrequency());
		}
	}
}
