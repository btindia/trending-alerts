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

	public AlarmHelper(Context context, long interval) {
		final AlarmManager m = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		Intent intent = new Intent(context, FetchTrendingSearchesService.class);
		PendingIntent pi = PendingIntent.getService(context, 0, intent, 0);

		// m.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(),
		// interval, pi);

		m.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
				SystemClock.elapsedRealtime(), interval, pi);

		if (LogHelper.isLoggable(LOG_TAG)) {
			Log.d(LOG_TAG, "Alarm set!");
		}
	}
}
