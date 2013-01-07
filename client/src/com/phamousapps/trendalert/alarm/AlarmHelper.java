package com.phamousapps.trendalert.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.phamousapps.trendalert.utils.LogHelper;

public class AlarmHelper {
	private static final String LOG_TAG = AlarmHelper.class.getSimpleName();

	private final Context mContext;
	private final AlarmManager mAlarmManager;

	public AlarmHelper(Context context) {
		mContext = context;
		mAlarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
	}

	public void setAlarm(long interval) {
		Intent intent = new Intent(mContext, FetchTrendingSearchesService.class);
		PendingIntent pi = PendingIntent.getService(mContext, 0, intent, 0);

		// m.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(),
		// interval, pi);

		mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
				SystemClock.elapsedRealtime(), interval * 1000, pi);

		if (LogHelper.isLoggable(LOG_TAG)) {
			Log.d(LOG_TAG, "Alarm set!");
		}
	}

	public void cancelAlarm() {

		Intent intent = new Intent(mContext, FetchTrendingSearchesService.class);
		PendingIntent pi = PendingIntent.getService(mContext, 0, intent, 0);

		mAlarmManager.cancel(pi);
	}
}
