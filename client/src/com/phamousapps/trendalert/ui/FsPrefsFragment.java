package com.phamousapps.trendalert.ui;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.phamousapps.trendalert.R;

public class FsPrefsFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.prefs);
	}
}
