package com.phamousapps.trendalert.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

public class RequestHelper {

	private static final String LOG_TAG = RequestHelper.class.getSimpleName();

	public static String simpleGet(final String requestUrl) {

		if (LogHelper.isLoggable(LOG_TAG)) {
			Log.d(LOG_TAG, "requestUrl: " + requestUrl);
		}

		final StringBuilder response = new StringBuilder();

		URL url;
		HttpURLConnection urlConnection = null;
		try {
			url = new URL(requestUrl);

			if (LogHelper.isLoggable(LOG_TAG)) {
				Log.d(LOG_TAG, "url: " + url.toString());
			}

			urlConnection = (HttpURLConnection) url.openConnection();

			InputStream in = new BufferedInputStream(
					urlConnection.getInputStream());

			response.append(InputStreamHelper.readInputStream(in));

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}

		return response.toString();
	}
}
