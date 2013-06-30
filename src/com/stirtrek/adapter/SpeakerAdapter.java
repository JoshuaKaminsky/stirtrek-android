package com.stirtrek.adapter;

import java.util.Comparator;
import java.util.List;

import stirtrek.activity.R;
import com.stirtrek.model.Speaker;
import android.content.Context;
import android.text.util.Linkify;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SpeakerAdapter extends BaseArrayAdapter<Speaker>{

//	private static String speakerPhotoUrl = "http://stirtrek.com/Content/Images/Speakers/";
	private List<Speaker> _speakers;	
	
	public SpeakerAdapter(Context context, List<Speaker> objects) {
		super(context, R.layout.speaker_list_item, objects);
		
		_speakers = objects;
		this.sort(new Comparator<Speaker>() {

			public int compare(Speaker lhs, Speaker rhs) {
				if(lhs == null && rhs == null)
					return 0;
				if(lhs == null)
					return -1;
				if(rhs == null)
					return 1;
				
				return lhs.Name.compareTo(rhs.Name);
			}
		});
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		
		Speaker speaker = _speakers.get(position);
		
		TextView textView = (TextView)view.findViewById(R.id.speaker_name);
		textView.setText(speaker.Name);
		
		textView = (TextView)view.findViewById(R.id.speaker_bio);
		textView.setText(speaker.Bio);
		
		Linkify.addLinks(textView, Linkify.EMAIL_ADDRESSES | Linkify.WEB_URLS);
		
//		ImageView imageView = (ImageView)view.findViewById(R.id.speaker_picture);		
//		new HttpGetImageAsyncTask(imageView).execute(speakerPhotoUrl + speaker.Name.replace(" ", "%20") + ".png");
		
		return view;
	}
}
