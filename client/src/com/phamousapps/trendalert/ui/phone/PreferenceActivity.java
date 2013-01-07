package com.phamousapps.trendalert.ui.phone;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.phamousapps.trendalert.R;
import com.phamousapps.trendalert.ui.FsPrefsFragment;

public class PreferenceActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_prefs);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Display the fragment as the main content.
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new FsPrefsFragment()).commit();
	}

}
