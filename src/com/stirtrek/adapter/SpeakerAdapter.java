package com.stirtrek.adapter;

import java.util.Comparator;
import java.util.List;

import stirtrek.activity.R;

import com.android.client.utilities.HttpGetImageAsyncTask;
import com.android.client.utilities.IResultCallback;
import com.android.client.utilities.Utilities;
import com.stirtrek.application.StirTrek.App;
import com.stirtrek.model.Speaker;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.util.Linkify;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SpeakerAdapter extends BaseArrayAdapter<Speaker>{

	private List<Speaker> _speakers;	
	
	public SpeakerAdapter(Context context, List<Speaker> speakers) {
		super(context, R.layout.speaker_list_item, speakers);
		
		_speakers = speakers;
		
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
		View view = super.getView(position, null, parent);
		
		final Speaker speaker = _speakers.get(position);
		
		TextView textView = (TextView)view.findViewById(R.id.speaker_name);
		textView.setText(speaker.Name);
		
		textView = (TextView)view.findViewById(R.id.speaker_bio);
		textView.setText(speaker.Bio);
		
		Linkify.addLinks(textView, Linkify.EMAIL_ADDRESSES | Linkify.WEB_URLS);
		
		final ImageView imageView = (ImageView)view.findViewById(R.id.speaker_picture);		

		Bitmap image = App.GetImageFromCache(Utilities.GetKey(speaker.ImageUrl));
		if(image != null) {
			imageView.setImageBitmap(image);
		} else {
			new HttpGetImageAsyncTask(new IResultCallback<Bitmap>() {
	
				public Class<Bitmap> GetType() {
					return Bitmap.class;
				}
	
				public void Callback(Bitmap result) {
					imageView.setImageBitmap(result);
	
				}
			}).execute(speaker.ImageUrl);
		}
		
		return view;
	}		
}
