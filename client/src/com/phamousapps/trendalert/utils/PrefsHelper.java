package com.phamousapps.trendalert.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.phamousapps.trendalert.R;

public class PrefsHelper {

	private static final String LOG_TAG = PrefsHelper.class.getSimpleName();

	private final String mSearchParam;
	private final boolean mAlertsEnabled;
	private final long mAlertsFrequency;
	private final boolean mSoundEnabled;
	private final int mSoundId;
	private final boolean mVibrationEnabled;
	private final String mVibrationIntensity;

	public PrefsHelper(Context context) {

		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);

		mSearchParam = sp.getString(
				context.getString(R.string.key_search_param), "");

		mAlertsEnabled = sp.getBoolean(
				context.getString(R.string.key_enable_alerts), true);

		String freq = sp.getString(
				context.getString(R.string.key_alert_frequency), "3600");
		mAlertsFrequency = Long.valueOf(freq);

		mSoundEnabled = sp.getBoolean(
				context.getString(R.string.key_enable_sound), true);

		String soundId = sp.getString(
				context.getString(R.string.key_alert_frequency), "1");

		if ("2".equals(soundId)) {
			mSoundId = R.raw.sound_02;
		} else if ("3".equals(soundId)) {
			mSoundId = R.raw.sound_03;
		} else {
			mSoundId = R.raw.sound_01;
		}

		mVibrationEnabled = sp.getBoolean(
				context.getString(R.string.key_enable_vibration), false);

		mVibrationIntensity = sp.getString(
				context.getString(R.string.key_intensity), "Low");

		if (LogHelper.isLoggable(LOG_TAG)) {
			Log.d(LOG_TAG, this.toString());
		}
	}

	@Override
	public String toString() {
		return "PrefsHelper [mSearchParam=" + mSearchParam
				+ ", mAlertsEnabled=" + mAlertsEnabled + ", mAlertsFrequency="
				+ mAlertsFrequency + ", mSoundEnabled=" + mSoundEnabled
				+ ", mSoundId=" + mSoundId + ", mVibrationEnabled="
				+ mVibrationEnabled + ", mVibrationIntensity="
				+ mVibrationIntensity + "]";
	}

	public String getSearchParam() {
		return mSearchParam;
	}

	public boolean isAlertsEnabled() {
		return mAlertsEnabled;
	}

	public long getAlertsFrequency() {
		return mAlertsFrequency;
	}

	public boolean isSoundEnabled() {
		return mSoundEnabled;
	}

	public int getSoundId() {
		return mSoundId;
	}

	public boolean isVibrationEnabled() {
		return mVibrationEnabled;
	}

	public String getVibrationIntensity() {
		return mVibrationIntensity;
	}
}
