package com.phamousapps.trendalert.notification;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.phamousapps.trendalert.data.Venue;

public class NotificationPackage implements Parcelable {

	public static final String ARG_KEY = "com.phamousapps.trendalert.NOTIFICATION_PACKAGE";

	private final ArrayList<Venue> mVenues;

	private static Gson sGson;

	public NotificationPackage(ArrayList<Venue> venues) {
		mVenues = venues;
	}

	private Gson getGson() {
		if (sGson == null) {
			synchronized (NotificationPackage.this) {
				if (sGson == null) {
					sGson = new Gson();
				}
			}
		}
		return sGson;
	}

	public ArrayList<Venue> getVenues() {
		return mVenues;
	}

	/**
	 * Methods necessary for parceling
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		String noteObject = getGson().toJson(mVenues);
		dest.writeString(noteObject);
	}

	private NotificationPackage(Parcel in) {
		String noteObject = in.readString();
		Venue[] array = getGson().fromJson(noteObject, Venue[].class);

		mVenues = new ArrayList<Venue>();
		for (Venue venue : array) {
			mVenues.add(venue);
		}
	}

	public static final Parcelable.Creator<NotificationPackage> CREATOR = new Parcelable.Creator<NotificationPackage>() {
		@Override
		public NotificationPackage createFromParcel(Parcel in) {
			return new NotificationPackage(in);
		}

		@Override
		public NotificationPackage[] newArray(int size) {
			return new NotificationPackage[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}
}
