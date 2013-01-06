package com.phamousapps.trendalert.ui.phone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.phamousapps.trendalert.R;
import com.phamousapps.trendalert.alarm.AlarmHelper;
import com.phamousapps.trendalert.data.Venue;
import com.phamousapps.trendalert.ui.SearchFragment;
import com.phamousapps.trendalert.ui.TrendingPlaceDetailFragment;
import com.phamousapps.trendalert.ui.TrendingPlaceListFragment;

/**
 * An activity representing a list of TrendingPlaces. This activity has
 * different presentations for handset and tablet-size devices. On handsets, the
 * activity presents a list of items, which when touched, lead to a
 * {@link TrendingPlaceDetailActivity} representing item details. On tablets,
 * the activity presents the list of items and item details side-by-side using
 * two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link TrendingPlaceListFragment} and the item details (if present) is a
 * {@link TrendingPlaceDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link TrendingPlaceListFragment.Callbacks} interface to listen for item
 * selections.
 */
public class TrendingPlaceListActivity extends FragmentActivity implements
		TrendingPlaceListFragment.Callbacks, SearchFragment.Callbacks {

	public static final String SEARCH_PARAM_KEY = "com.phamousapps.trendalert.SEARCH_PARAM";
	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trendingplace_list);

		new AlarmHelper(this, 1000 * 30);

		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		String searchParam = sp.getString(SEARCH_PARAM_KEY, "");

		Bundle arguments = new Bundle();
		arguments.putString(TrendingPlaceListFragment.ARG_ITEM_ID, searchParam);

		if (findViewById(R.id.trendingplace_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			TrendingPlaceListFragment fragment = new TrendingPlaceListFragment();
			fragment.setArguments(arguments);
			// fragment.setActivateOnItemClick(true);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.trendingplace_list, fragment).commit();

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			// ((TrendingPlaceListFragment) getSupportFragmentManager()
			// .findFragmentById(R.id.trendingplace_list))
			// .setActivateOnItemClick(true);
		} else {
			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			// ((TrendingPlaceListFragment) getSupportFragmentManager()
			// .findFragmentById(R.id.trendingplace_list))
			// .setArguments(arguments);
		}

		// TODO: If exposing deep links into your app, handle intents here.
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		Intent detailIntent = new Intent(this, SearchActivity.class);
		startActivity(detailIntent);

		return super.onMenuItemSelected(featureId, item);
	}

	/**
	 * Callback method from {@link TrendingPlaceListFragment.Callbacks}
	 * indicating that the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(Venue venue) {

		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(TrendingPlaceDetailFragment.ARG_ITEM_ID,
					venue.getId());
			TrendingPlaceDetailFragment fragment = new TrendingPlaceDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.trendingplace_detail_container, fragment)
					.commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this,
					TrendingPlaceDetailActivity.class);
			detailIntent.putExtra(TrendingPlaceDetailFragment.ARG_ITEM_ID,
					venue.getId());
			startActivity(detailIntent);
		}
	}

	@Override
	public void onEditTextComplete(String string) {

		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = sp.edit();

		editor.putString(SEARCH_PARAM_KEY, string);
		editor.commit();

		Toast.makeText(this, "Text Received: " + string, Toast.LENGTH_SHORT)
				.show();

		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(TrendingPlaceListFragment.ARG_ITEM_ID, string);

			TrendingPlaceListFragment fragment = new TrendingPlaceListFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.trendingplace_list, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			// Intent detailIntent = new Intent(this,
			// TrendingPlaceDetailActivity.class);
			// detailIntent.putExtra(TrendingPlaceDetailFragment.ARG_ITEM_ID,
			// venue.getId());
			// startActivity(detailIntent);
		}
	}
}
