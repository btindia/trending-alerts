package com.phamousapps.trendalert.alarm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.gson.Gson;
import com.phamousapps.trendalert.data.FsResponse;
import com.phamousapps.trendalert.data.Venue;
import com.phamousapps.trendalert.notification.NotificationPackage;
import com.phamousapps.trendalert.notification.NotificationReceiver;
import com.phamousapps.trendalert.utils.FsSettings;
import com.phamousapps.trendalert.utils.LogHelper;
import com.phamousapps.trendalert.utils.PrefsHelper;
import com.phamousapps.trendalert.utils.RequestHelper;

public class FetchTrendingSearchesService extends IntentService {

	private static final String LOG_TAG = FetchTrendingSearchesService.class
			.getSimpleName();

	public FetchTrendingSearchesService() {
		super(LOG_TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		if (LogHelper.isLoggable(LOG_TAG)) {
			Log.d(LOG_TAG, "Fetching new contents!");
		}

		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		String provider = locationManager.getBestProvider(criteria, true);
		locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);

		PrefsHelper ph = new PrefsHelper(this);
		if (ph.isAlertsEnabled()) {

			Intent noteAction = new Intent(NotificationReceiver.ACTION);

			NotificationPackage np = new NotificationPackage(fetchVenues(
					location, ph.getSearchParam()));
			noteAction.putExtra(NotificationPackage.ARG_KEY, np);

			sendBroadcast(noteAction);

		}
	}

	private ArrayList<Venue> fetchVenues(Location location, String queryParam) {
		String lat = String.valueOf(location.getLatitude());
		String lon = String.valueOf(location.getLongitude());

		StringBuilder builder = new StringBuilder();
		builder.append("&ll=").append(lat).append(',').append(lon);
		String locationParams = builder.toString();

		builder = new StringBuilder();
		builder.append(FsSettings.URL_TRENDING_AUTH).append(locationParams);
		String trendingUrl = builder.toString();

		String trendingResponse = RequestHelper.simpleGet(trendingUrl);

		Gson gson = new Gson();
		FsResponse trending = gson.fromJson(trendingResponse, FsResponse.class);

		Venue[] trendingVenues = trending.getResponse().getVenues();

		Set<String> searchKeys = new HashSet<String>();
		if (!queryParam.isEmpty()) {
			builder = new StringBuilder();
			builder.append(FsSettings.URL_SEARCH_AUTH).append(locationParams);
			builder.append("&query=").append(queryParam);
			String searchUrl = builder.toString();

			String searchResponse = RequestHelper.simpleGet(searchUrl);

			FsResponse search = gson.fromJson(searchResponse, FsResponse.class);

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
				Log.d(LOG_TAG,
						"name:" + venue.getName() + ", id:" + venue.getId());
			}
		}

		return venueList;
	}
}
