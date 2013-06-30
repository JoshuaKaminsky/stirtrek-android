package com.stirtrek.adapter;

import java.util.List;

import stirtrek.activity.R;
import com.stirtrek.model.Speaker;
import android.app.Dialog;
import com.android.client.utilities.HttpGetImageAsyncTask;
import com.android.client.utilities.IResultCallback;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class SessionSpeakerAdapter extends BaseArrayAdapter<Speaker>{

	private static String speakerPhotoUrl = "http://stirtrek.com/Content/Images/Speakers/";
		
	public SessionSpeakerAdapter(Context context, List<Speaker> objects) {
		super(context, R.layout.session_speaker, objects);
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {		
		TextView view = (TextView) super.getView(position, convertView, parent);
		
		final Speaker speaker = getItem(position);
		view.setText(speaker.Name);
		
		view.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Dialog details = new Dialog(v.getContext());
				details.requestWindowFeature(Window.FEATURE_NO_TITLE);
				details.setCanceledOnTouchOutside(true);
				details.setCancelable(true);
				details.setContentView(R.layout.speaker_details);
				
				TextView textView = (TextView)details.findViewById(R.id.speaker_details_name);
				textView.setText(speaker.Name);
				
				textView = (TextView)details.findViewById(R.id.speaker_details_bio);
				textView.setText(speaker.Bio);
				
				Linkify.addLinks(textView, Linkify.EMAIL_ADDRESSES | Linkify.WEB_URLS);
				
				final ImageView imageView = (ImageView)details.findViewById(R.id.speaker_details_picture);	
				
				new HttpGetImageAsyncTask(new IResultCallback<Bitmap>() {
					
					public Class<Bitmap> GetType() {
						return Bitmap.class;
					}
					
					public void Callback(Bitmap result) {
						imageView.setImageBitmap(result);
						
					}
				}).execute(speakerPhotoUrl + speaker.Name.replace(" ", "%20") + ".png");
				
				details.show();
				
			}
		});
		
		return view;
	}
}
