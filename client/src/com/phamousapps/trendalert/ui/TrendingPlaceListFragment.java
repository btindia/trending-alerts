package com.phamousapps.trendalert.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.gson.Gson;
import com.phamousapps.trendalert.R;
import com.phamousapps.trendalert.data.FsResponse;
import com.phamousapps.trendalert.data.Venue;
import com.phamousapps.trendalert.utils.FsSettings;
import com.phamousapps.trendalert.utils.LogHelper;
import com.phamousapps.trendalert.utils.PrefsHelper;
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
public class TrendingPlaceListFragment extends Fragment implements
		LocationListener {

	private static final String LOG_TAG = TrendingPlaceListFragment.class
			.getSimpleName();

	public static final String ARG_ITEM_ID = "item_id";

	private LocationManager mLocationManager;
	private VenueAdapter mAdapter;
	private PrefsHelper mPrefsHelper;

	private ListView mListView;
	private ViewSwitcher mViewSwitcher;
	private TextView mErrorTv;

	private String mSearchParam;

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

		mPrefsHelper = new PrefsHelper(getActivity());
		mSearchParam = mPrefsHelper.getSearchParam();

		mLocationManager = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);

		// boolean enabled = mLocationManager
		// .isProviderEnabled(LocationManager.GPS_PROVIDER);
		// Check if enabled and if not send user to the GSP settings
		// Better solution would be to display a dialog and suggesting to
		// go to the settings
		// if (!enabled) {
		// Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		// startActivity(intent);
		// }

		Criteria criteria = new Criteria();
		String provider = mLocationManager.getBestProvider(criteria, true);
		mLocationManager.requestSingleUpdate(provider, this, null);
	}

	@Override
	public void onResume() {
		super.onResume();

		// Get saved value, may be new
		PrefsHelper ph = new PrefsHelper(getActivity());
		String searchParam = ph.getSearchParam();

		if (!mSearchParam.equals(searchParam)) {
			mPrefsHelper = ph;
			mSearchParam = searchParam;
			mViewSwitcher.showNext();
			Criteria criteria = new Criteria();
			String provider = mLocationManager.getBestProvider(criteria, true);
			mLocationManager.requestSingleUpdate(provider, this, null);
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized activated item position.
		if ((savedInstanceState != null)
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_list_result, container,
				false);

		mListView = (ListView) root.findViewById(R.id.listview);
		mViewSwitcher = (ViewSwitcher) root.findViewById(R.id.switcher);
		mErrorTv = (TextView) root.findViewById(R.id.error);

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View v,
					int position, long id) {
				mCallbacks.onItemSelected((Venue) mAdapter.getItem(position));
			}
		});

		return root;
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
		mListView
				.setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			mListView.setItemChecked(mActivatedPosition, false);
		} else {
			mListView.setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}

	public void fetchNewVenues(final Location location) {
		new AsyncTask<Void, Void, ArrayList<Venue>>() {

			@Override
			protected ArrayList<Venue> doInBackground(Void... params) {

				String lat = String.valueOf(location.getLatitude());
				String lon = String.valueOf(location.getLongitude());

				StringBuilder builder = new StringBuilder();
				builder.append("&ll=").append(lat).append(',').append(lon);
				String locationParams = builder.toString();

				builder = new StringBuilder();
				builder.append(FsSettings.URL_TRENDING_AUTH).append(
						locationParams);
				String trendingUrl = builder.toString();

				String trendingResponse = RequestHelper.simpleGet(trendingUrl);

				Gson gson = new Gson();
				FsResponse trending = gson.fromJson(trendingResponse,
						FsResponse.class);

				Venue[] trendingVenues = trending.getResponse().getVenues();
				String queryParam = mPrefsHelper.getEncodedSearchParam();

				Set<String> searchKeys = new HashSet<String>();
				if (!queryParam.isEmpty()) {
					builder = new StringBuilder();
					builder.append(FsSettings.URL_SEARCH_AUTH).append(
							locationParams);
					builder.append("&query=").append(queryParam);
					String searchUrl = builder.toString();

					String searchResponse = RequestHelper.simpleGet(searchUrl);

					FsResponse search = gson.fromJson(searchResponse,
							FsResponse.class);

					Venue[] searchVenues = search.getResponse().getVenues();

					for (Venue venue : searchVenues) {
						searchKeys.add(venue.getId());

						if (LogHelper.isLoggable(LOG_TAG)) {
							Log.d(LOG_TAG, "Adding set key: " + venue.getId());
						}
					}
				}

				Map<String, Venue> trendingSearches = new HashMap<String, Venue>();
				for (Venue venue : trendingVenues) {
					final String id = venue.getId();

					if (LogHelper.isLoggable(LOG_TAG)) {
						Log.d(LOG_TAG, "Checking trendd key: " + venue.getId());
					}

					if (searchKeys.contains(id) || searchKeys.isEmpty()) {
						// If a search matched or there was no search result
						trendingSearches.put(id, venue);
					}
				}

				ArrayList<Venue> venueList = new ArrayList<Venue>();
				for (String key : trendingSearches.keySet()) {
					venueList.add(trendingSearches.get(key));
				}

				if (LogHelper.isLoggable(LOG_TAG)) {

					Log.d(LOG_TAG, "Merged items: " + venueList.size());
					for (Venue venue : venueList) {
						Log.d(LOG_TAG, "name:" + venue.getName() + ", id:"
								+ venue.getId());
					}
				}

				return venueList;

			}

			@Override
			protected void onPostExecute(ArrayList<Venue> result) {
				super.onPostExecute(result);

				if (!result.isEmpty()) {
					mAdapter = new VenueAdapter(getActivity(), result);
					mListView.setAdapter(mAdapter);
					setActivateOnItemClick(true);

				} else {

					mListView.setVisibility(View.INVISIBLE);
					mErrorTv.setVisibility(View.VISIBLE);
				}

				mViewSwitcher.showNext();
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
