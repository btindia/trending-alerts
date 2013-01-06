package com.phamousapps.trendalert.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.phamousapps.trendalert.utils.FsSettings;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		new AlarmHelper(context, FsSettings.INTERVAL);
	}
}
