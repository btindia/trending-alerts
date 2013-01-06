package com.phamousapps.trendalert.ui.phone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.phamousapps.trendalert.R;
import com.phamousapps.trendalert.data.Venue;
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
		TrendingPlaceListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trendingplace_list);

		if (findViewById(R.id.trendingplace_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((TrendingPlaceListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.trendingplace_list))
					.setActivateOnItemClick(true);
		}

		// TODO: If exposing deep links into your app, handle intents here.
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
			arguments.putString(TrendingPlaceDetailFragment.ARG_ITEM_ID, venue.getId());
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
			detailIntent.putExtra(TrendingPlaceDetailFragment.ARG_ITEM_ID, venue.getId());
			startActivity(detailIntent);
		}
		
	}
}