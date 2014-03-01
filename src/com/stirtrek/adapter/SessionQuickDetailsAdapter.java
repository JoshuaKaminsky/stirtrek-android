package com.stirtrek.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import stirtrek.activity.R;
import com.stirtrek.model.Session;
import com.stirtrek.model.Speaker;
import com.stirtrek.model.Track;

public class SessionQuickDetails extends BaseArrayAdapter<Session> {

	private SparseArray<Track> _tracks;
	private SparseArray<Speaker> _speakers;
	
	public SessionQuickDetails(Context context,
			Session[] objects, Track[] tracks, Speaker[] speakers) {
		super(context, R.layout.session_quick_details, objects);
		
		_tracks = new SparseArray<Track>();// new HashMap<Integer, Track>();
		for (Track track : tracks) {
			_tracks.put(track.Id, track);
		}

		_speakers = new SparseArray<Speaker>();// new HashMap<Integer, Speaker>();
		for (Speaker speaker : speakers) {
			_speakers.put(speaker.Id, speaker);
		}
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		
		final Session session = getItem(position);
		
		Track track = _tracks.get(session.TrackId);
		
		List<Speaker> speakers = new ArrayList<Speaker>();
		for (int speakerId : session.SpeakerIds) {
			speakers.add(_speakers.get(speakerId));
		}
		
		TextView textView = (TextView) view.findViewById(R.id.session_quick_name);
		if(textView != null) {
			textView.setText(session.Name);
		}
		textView = (TextView) view.findViewById(R.id.session_quick_track);
		if(textView != null) {
			textView.setText(track.Name);
		}

		if (speakers.size() > 0) {
			textView = (TextView) view.findViewById(R.id.session_quick_speaker_primary);
			if(textView != null) {				
				textView.setText(speakers.get(0).Name);
			}
			
			if (speakers.size() > 1) {

				textView = (TextView) view.findViewById(R.id.session_quick_speaker_secondary);
				if(textView != null) {					
					textView.setText(speakers.get(1).Name);
				}
			}
		}

		return view;
	}
}
