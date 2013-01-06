package com.phamousapps.trendalert.notification;

import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.phamousapps.trendalert.R;
import com.phamousapps.trendalert.data.Venue;
import com.phamousapps.trendalert.ui.phone.TrendingPlaceListActivity;
import com.phamousapps.trendalert.utils.LogHelper;

/**
 * Handles the displaying of all Notification within the application. You can
 * simulate receiving a notification using the command line tools. See the
 * NOTIFCATIONS_README.txt for more information
 * 
 * @author kevingrant
 * 
 */
public class NotificationReceiver extends BroadcastReceiver {

	private static final String LOG_TAG = NotificationReceiver.class
			.getSimpleName();

	public static final String ACTION = "com.phamousapps.trendalert.NOTIFICATION_RECEIVED";

	private Context mContext;

	@Override
	public void onReceive(Context context, Intent intent) {
		mContext = context;

		Bundle extras = intent.getExtras();
		if (extras != null) {

			if (LogHelper.isLoggable(LOG_TAG)) {
				Log.d(LOG_TAG, "Received my broadcast!");
			}

			NotificationPackage notePackage = extras
					.getParcelable(NotificationPackage.ARG_KEY);

			Intent launchIntent = new Intent(context,
					TrendingPlaceListActivity.class);

			PendingIntent pi = PendingIntent.getActivity(context, 0,
					launchIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
			buildAndShowNotification(pi, notePackage, 0);
		}
	}

	private void buildAndShowNotification(PendingIntent pIntent,
			NotificationPackage notePackage, int id) {

		Notification notification;

		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				mContext);

		List<Venue> venues = notePackage.getVenues();

		if (!venues.isEmpty()) {
			builder.setContentTitle(venues.size() + " places are trending!");
			builder.setContentText("Touch to view");

			builder.setSmallIcon(R.drawable.ic_launcher);
			builder.setContentIntent(pIntent);

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

				NotificationCompat.InboxStyle bigInbox = new NotificationCompat.InboxStyle(
						builder);

				int length = venues.size();
				int size = 5;
				if (length < size) {
					size = length;
				} else {
					int extra = length - size;
					bigInbox.setSummaryText("+" + extra + " more");
				}

				for (int i = 0; i < size; i++) {
					bigInbox.addLine(venues.get(i).getName());
				}

				notification = bigInbox.build();
			} else {
				notification = builder.build();
			}

			notification.defaults |= Notification.DEFAULT_LIGHTS;
			notification.defaults |= Notification.DEFAULT_VIBRATE;

			// Hide the notification after its selected
			notification.flags |= Notification.FLAG_AUTO_CANCEL;

			NotificationManager notificationManager = (NotificationManager) mContext
					.getSystemService(Context.NOTIFICATION_SERVICE);

			notificationManager.notify(id, notification);
		}
	}
}
