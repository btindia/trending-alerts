package com.phamousapps.trendalert.ui;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.phamousapps.trendalert.R;
import com.phamousapps.trendalert.alarm.AlarmHelper;
import com.phamousapps.trendalert.utils.LogHelper;

public class FsPrefsFragment extends PreferenceFragment implements
		OnSharedPreferenceChangeListener {

	private static final String LOG_TAG = FsPrefsFragment.class.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.prefs);

		SharedPreferences sPrefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		sPrefs.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {

		if (LogHelper.isLoggable(LOG_TAG)) {
			Log.d(LOG_TAG, "Key changed: " + key);
		}

		String enableAlertsKey = getString(R.string.key_enable_alerts);
		String alertsFreqKey = getString(R.string.key_alert_frequency);

		if (key.equals(enableAlertsKey) || key.equals(alertsFreqKey)) {

			boolean enabled = sharedPreferences.getBoolean(
					getString(R.string.key_enable_alerts), true);

			AlarmHelper ah = new AlarmHelper(getActivity());
			ah.cancelAlarm();
			if (LogHelper.isLoggable(LOG_TAG)) {
				Log.d(LOG_TAG, "Cancelled previous alarm");
			}

			if (enabled) {

				if (LogHelper.isLoggable(LOG_TAG)) {
					Log.d(LOG_TAG, "Scheduling new alarm");
				}

				String freq = sharedPreferences.getString(
						getString(R.string.key_alert_frequency), "3600");
				long alertsFrequency = Long.valueOf(freq);

				ah.setAlarm(alertsFrequency);
			} else {
				if (LogHelper.isLoggable(LOG_TAG)) {
					Log.d(LOG_TAG, "Alarm was disabled, not rescheduling");
				}

			}
		}
	}
}
