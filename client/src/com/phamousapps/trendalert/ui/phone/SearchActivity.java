package com.phamousapps.trendalert.ui.phone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;

import com.phamousapps.trendalert.R;
import com.phamousapps.trendalert.ui.SearchFragment;
import com.phamousapps.trendalert.ui.TrendingPlaceListFragment;

public class SearchActivity extends FragmentActivity implements
		SearchFragment.Callbacks {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_search);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(
					TrendingPlaceListFragment.ARG_ITEM_ID,
					getIntent().getStringExtra(
							TrendingPlaceListFragment.ARG_ITEM_ID));

			SearchFragment fragment = new SearchFragment();
			fragment.setArguments(arguments);

			getSupportFragmentManager().beginTransaction()
					.add(R.id.search_container, fragment).commit();
		}
	}

	@Override
	public void onEditTextComplete(String string) {

		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = sp.edit();

		editor.putString(TrendingPlaceListActivity.SEARCH_PARAM_KEY, string);
		editor.commit();

		Intent listIntent = new Intent(this, TrendingPlaceListActivity.class);
		listIntent.putExtra(TrendingPlaceListFragment.ARG_ITEM_ID, string);
		startActivity(listIntent);
		finish();

	}
}
