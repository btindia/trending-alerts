package com.phamousapps.trendalert.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.phamousapps.trendalert.R;
import com.phamousapps.trendalert.data.FsResponse;
import com.phamousapps.trendalert.data.Venue;
import com.phamousapps.trendalert.utils.FsSettings;
import com.phamousapps.trendalert.utils.LogHelper;
import com.phamousapps.trendalert.utils.RequestHelper;

/**
 * A list fragment representing a list of TrendingPlaces. This fragment also
 * supports tablet devices by allowing list items to be given an 'activated'
 * state upon selection. This helps indicate which item is currently being
 * viewed in a {@link TrendingPlaceDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class TrendingPlaceListFragment extends ListFragment implements
		LocationListener {

	private static final String LOG_TAG = TrendingPlaceListFragment.class
			.getSimpleName();

	private LocationManager mLocationManager;
	private VenueAdapter mAdapter;

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(Venue venue);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(Venue venue) {
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public TrendingPlaceListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mLocationManager = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);
		boolean enabled = mLocationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		// Check if enabled and if not send user to the GSP settings
		// Better solution would be to display a dialog and suggesting to
		// go to the settings
		if (!enabled) {
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(intent);
		}

		Criteria criteria = new Criteria();
		String provider = mLocationManager.getBestProvider(criteria, true);
		mLocationManager.requestSingleUpdate(provider, this, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);

		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		mCallbacks.onItemSelected((Venue) mAdapter.getItem(position));
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}

	public void fetchNewVenues(final Location location) {
		new AsyncTask<Void, Void, List<Venue>>() {

			@Override
			protected List<Venue> doInBackground(Void... params) {

				String lat = String.valueOf(location.getLatitude());
				String lon = String.valueOf(location.getLongitude());

				StringBuilder builder = new StringBuilder();
				builder.append("&ll=").append(lat).append(',').append(lon);
				String locationParams = builder.toString();

				builder = new StringBuilder();
				builder.append(FsSettings.URL_TRENDING_AUTH).append(locationParams);
				String trendingUrl = builder.toString();

				builder = new StringBuilder();
				builder.append(FsSettings.URL_SEARCH_AUTH).append(locationParams);
				builder.append("&query=art");
				String searchUrl = builder.toString();
				
				String trendingResponse = RequestHelper.simpleGet(trendingUrl);
				String searchResponse = RequestHelper.simpleGet(searchUrl);
				
				Gson gson = new Gson();
				FsResponse trending = gson.fromJson(trendingResponse,
						FsResponse.class);

				FsResponse search = gson.fromJson(searchResponse,
						FsResponse.class);
				
				Venue[] trendingVenues = trending.getResponse()
						.getVenues();
				
				Venue[] searchVenues = search.getResponse()
						.getVenues();
				
				Set<String> searchKeys = new HashSet<String>();

				for (Venue venue : searchVenues) {
					searchKeys.add(venue.getId());
					
					if (LogHelper.isLoggable(LOG_TAG)) {
						Log.d(LOG_TAG, "Adding set key: " + venue.getId());
					}
				}
				
				Map<String, Venue> trendingSearches = new HashMap<String, Venue>();
				for (Venue venue : trendingVenues) {
					final String id = venue.getId();
					
					if (LogHelper.isLoggable(LOG_TAG)) {
						Log.d(LOG_TAG, "Checking trendd key: " + venue.getId());
					}
					
					if(searchKeys.contains(id)){
						trendingSearches.put(id, venue);
					}
				}
				
				List<Venue> venueList = new ArrayList<Venue>();
				for (String  key : trendingSearches.keySet()) {
					venueList.add(trendingSearches.get(key));
				}

				if (LogHelper.isLoggable(LOG_TAG)) {

					Log.d(LOG_TAG, "Merged items: " + venueList.size());
					for (Venue venue : venueList) {
						Log.d(LOG_TAG, "name:" + venue.getName() + ", id:" + venue.getId());	
					}
				}

				
				return venueList;

			}

			@Override
			protected void onPostExecute(List<Venue> result) {
				super.onPostExecute(result);

				if (result != null) {
					mAdapter = new VenueAdapter(getActivity(), result);
					setListAdapter(mAdapter);
				}

			}
		}.execute();
	}

	@Override
	public void onLocationChanged(Location location) {
		fetchNewVenues(location);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
}
