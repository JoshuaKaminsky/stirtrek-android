package com.stirtrek.adapter;

import java.util.ArrayList;
import java.util.List;

import stirtrek.activity.R;

import com.stirtrek.activity.MainActivity;
import com.stirtrek.activity.SessionInfoActivity;
import com.stirtrek.activity.SpeakerInfoActivity;
import com.stirtrek.activity.SplashScreen;
import com.stirtrek.application.StirTrek.App;
import com.stirtrek.common.SessionSorter;
import com.stirtrek.model.Interest.Interests;
import com.stirtrek.model.Session;
import com.stirtrek.model.Speaker;
import com.stirtrek.model.TimeSlot;
import com.stirtrek.model.Track;

import android.app.Dialog;

import com.android.client.utilities.CollectionUtilities;
import com.android.client.utilities.HttpGetImageAsyncTask;
import com.android.client.utilities.JsonUtilities;
import com.android.contract.IResultCallback;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SessionAdapter extends AlternatingListViewAdapter<Session> implements OnClickListener {

	private SparseArray<TimeSlot> _timeslots;
	private SparseArray<Speaker> _speakers;

	public SessionAdapter(Context context, Session[] objects, 
			TimeSlot[] timeSlots, Speaker[] speakers) {
		super(context, R.layout.schedule_list_item, objects);

		_timeslots = new SparseArray<TimeSlot>();// new HashMap<Integer, TimeSlot>();
		for (TimeSlot timeslot : timeSlots) {
			_timeslots.put(timeslot.Id, timeslot);
		}

		_speakers = new SparseArray<Speaker>();// new HashMap<Integer, Speaker>();
		for (Speaker speaker : speakers) {
			_speakers.put(speaker.Id, speaker);
		}

		this.sort(new SessionSorter(timeSlots));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//cant send in convert view because it will lose the checked state 
		View view = super.getView(position, null, null);

		final Session session = getItem(position);
		TimeSlot timeSlot = _timeslots.get(session.TimeSlotId);

		List<Speaker> speakers = new ArrayList<Speaker>();
		for (int speakerId : session.SpeakerIds) {
			speakers.add(_speakers.get(speakerId));
		}

		TextView textView = (TextView) view.findViewById(R.id.schedule_time);
		textView.setText(timeSlot.GetName());

		textView = (TextView) view.findViewById(R.id.session_name);
		textView.setText(session.Name);
		
		if (speakers.size() > 0) {
			textView = (TextView) view.findViewById(R.id.session_speaker_primary);
			
			SpannableString contentUnderline = new SpannableString(speakers.get(0).Name);
			contentUnderline.setSpan(new UnderlineSpan(), 0, contentUnderline.length(), 0);
			
			textView.setText(contentUnderline);
			textView.setOnClickListener(this);
			textView.setTag(speakers.get(0));

			if (speakers.size() > 1) {

				textView = (TextView) view
						.findViewById(R.id.session_speaker_secondary);
				
				contentUnderline = new SpannableString(speakers.get(1).Name);
				contentUnderline.setSpan(new UnderlineSpan(), 0, contentUnderline.length(), 0);
				
				textView.setVisibility(View.VISIBLE);
				textView.setText(contentUnderline);
				textView.setOnClickListener(this);
				textView.setTag(speakers.get(1));
			}
		}

		ToggleButton favorite = (ToggleButton) view.findViewById(R.id.session_star);
		favorite.setChecked(App.IsInterest(session.Id));
		favorite.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				ContentResolver resolver = getContext().getContentResolver();

				if (isChecked) {
					// add to the db
					ContentValues values = new ContentValues();
					values.put(Interests.SESSIONID, session.Id);
					values.put(Interests.TIMESLOTID, session.TimeSlotId);
					resolver.insert(Interests.CONTENT_URI, values);

				} else {
					// remove from the db
					String where = Interests.SESSIONID + " = ?";
					String[] args = new String[] { Integer.toString(session.Id) };
					resolver.delete(Interests.CONTENT_URI, where, args);
					App.RefreshInterests(resolver);
				}

				App.RefreshInterests(resolver);
			}
		});

		return view;
	}

	public void onClick(View v) {
		//go to speaker details
		Context context = v.getContext();
		
    	Intent intent = new Intent(context, SpeakerInfoActivity.class);
    	
    	Speaker speaker = (Speaker)v.getTag();
		String speakerData = JsonUtilities.getJson(speaker);		
		intent.putExtra("SpeakerData", speakerData);
    	
    	context.startActivity(intent);
	}
}
