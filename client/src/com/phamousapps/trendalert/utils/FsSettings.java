package com.phamousapps.trendalert.utils;

import android.app.AlarmManager;

public class FsSettings {

	public static final String CLIENT_ID = "YOUR_CLIENT_ID";
	public static final String CLIENT_SECRET = "YOUR_CLIENT_SECRET";

	public static final String URL_TRENDING = "https://api.foursquare.com/v2/venues/trending";
	public static final String URL_SEARCH = "https://api.foursquare.com/v2/venues/search";

	public static String URL_TRENDING_AUTH;
	public static String URL_SEARCH_AUTH;

	public static final long INTERVAL = AlarmManager.INTERVAL_HALF_HOUR;

	static {
		StringBuilder builder = new StringBuilder();
		builder.append("?client_id=").append(CLIENT_ID);
		builder.append("&client_secret=").append(CLIENT_SECRET);
		builder.append("&v=20130101");
		String auth = builder.toString();

		builder = new StringBuilder();
		builder.append(URL_TRENDING).append(auth);
		URL_TRENDING_AUTH = builder.toString();

		builder = new StringBuilder();
		builder.append(URL_SEARCH).append(auth);
		URL_SEARCH_AUTH = builder.toString();
	}
}