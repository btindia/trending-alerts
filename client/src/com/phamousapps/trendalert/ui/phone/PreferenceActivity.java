package com.phamousapps.trendalert.ui.phone;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;

import com.phamousapps.trendalert.ui.FsPrefsFragment;
import com.phamousapps.trendalert.utils.LogHelper;

public class PreferenceActivity extends FragmentActivity {

	private static final String LOG_TAG = PreferenceActivity.class
			.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Display the fragment as the main content.
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new FsPrefsFragment()).commit();

		if (LogHelper.isLoggable(LOG_TAG)) {
			Log.d(LOG_TAG, "Preference Activity Started");
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
