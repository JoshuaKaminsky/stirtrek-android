package com.stirtrek.adapter;

import stirtrek.activity.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.client.utilities.CollectionUtilities;
import com.stirtrek.model.Session;
import com.stirtrek.model.Track;

public class InterestAdapter extends BaseArrayAdapter<Session> {

	private Track[] _tracks;
	
	public InterestAdapter(Context context, Session[] objects, Track[] tracks) {
		super(context, R.layout.interest_list_item, objects);
		
		_tracks = tracks;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		
		final Session session = getItem(position);
		Track track = CollectionUtilities.GetItem(session.TrackId, _tracks);
		 	
		TextView textView = (TextView)view.findViewById(R.id.interest_name);
		textView.setText(session.Name);
		
		textView = (TextView)view.findViewById(R.id.interest_location);	
		textView.setText(track.Location);
		
		textView = (TextView)view.findViewById(R.id.interest_track);	
		if(track != null) {	
			textView.setText(track.Name);
		}
		else {
			return view;
		}
		
		return view;
	}
}
