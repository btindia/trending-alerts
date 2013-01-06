package com.phamousapps.trendalert.ui;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.phamousapps.trendalert.R;
import com.phamousapps.trendalert.data.Venue;

public class VenueAdapter extends BaseAdapter {

	private Context mContext;
	private List<Venue> mVenues;

	public VenueAdapter(Context context, List<Venue> venues) {
		mVenues = venues;
		mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;

		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(R.layout.list_item_venue, parent,
					false);
		}

		TextView id, name, hereNow, distance;
		id = (TextView) view.findViewById(R.id.v_id);
		name = (TextView) view.findViewById(R.id.v_name);
		hereNow = (TextView) view.findViewById(R.id.v_here_now);
		distance = (TextView) view.findViewById(R.id.v_distance);

		id.setText(mVenues.get(position).getId());
		name.setText(mVenues.get(position).getName());

		String count = String
				.valueOf(mVenues.get(position).getHereNow().getCount());
		hereNow.setText(count);

		String distanceVal = String.valueOf(mVenues.get(position).getLocation()
				.getDistance());
		distance.setText(distanceVal);

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
