package com.phamousapps.trendalert.ui;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.phamousapps.trendalert.R;
import com.phamousapps.trendalert.data.FsCategory;
import com.phamousapps.trendalert.data.Venue;

public class VenueAdapter extends BaseAdapter {

	private final Context mContext;
	private final List<Venue> mVenues;

	public VenueAdapter(Context context, List<Venue> venues) {
		mVenues = venues;
		mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;

		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(
					R.layout.list_item_venue, parent, false);
		}

		TextView id, name, hereNow, distance, category;
		id = (TextView) view.findViewById(R.id.v_id);
		name = (TextView) view.findViewById(R.id.v_name);
		hereNow = (TextView) view.findViewById(R.id.v_here_now);
		distance = (TextView) view.findViewById(R.id.v_distance);
		category = (TextView) view.findViewById(R.id.v_category);

		final Venue venue = mVenues.get(position);

		id.setText(venue.getId());
		name.setText(venue.getName());

		String count = String.valueOf(venue.getHereNow().getCount());
		hereNow.setText(mContext.getString(R.string.people_here_now) + count);

		String distanceVal = String.valueOf(venue.getLocation().getDistance());
		distance.setText(distanceVal);

		FsCategory[] cats = venue.getCategories();
		StringBuilder catBuilder = new StringBuilder();

		for (FsCategory fsCategory : cats) {
			catBuilder.append(fsCategory.getName()).append(',');
		}
		int lastComma = catBuilder.lastIndexOf(",");
		catBuilder.deleteCharAt(lastComma);

		category.setText(catBuilder.toString());

		return view;
	}

	@Override
	public int getCount() {
		return mVenues.size();
	}

	@Override
	public Object getItem(int position) {
		return mVenues.get(position);
	}

	@Override
	public long getItemId(int position) {
		try {
			return Long.valueOf(mVenues.get(position).getId());
		} catch (NumberFormatException e) {
			return -1;
		}
	}
}
