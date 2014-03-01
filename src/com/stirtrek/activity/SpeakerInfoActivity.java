package com.stirtrek.activity;

import stirtrek.activity.R;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.client.utilities.HttpGetImageAsyncTask;
import com.android.client.utilities.JsonUtilities;
import com.android.client.utilities.Utilities;
import com.stirtrek.application.StirTrek.App;
import com.stirtrek.model.Session;
import com.stirtrek.model.Speaker;

public class SpeakerInfoActivity extends BaseActivity {

	private Session[] _sessions;
	private Speaker _speaker;
	
	public SpeakerInfoActivity() {
		super(R.layout.speaker_details);			
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String speakerData = getIntent().getExtras().getString("SpeakerData");
		
		_speaker = JsonUtilities.parseJson(speakerData, Speaker.class);
		
		_sessions = App.GetResponse().Sessions;
		
		Refresh();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		Refresh();
	}

	public void goBack(View v) {
		finish();
	}
	
	private void Refresh() {
		TextView textView = null;
		for (Session session : _sessions) {
			if(session.SpeakerIds.contains(_speaker.Id)) {
		/*		textView = (TextView)findViewById(R.id.session_details_title);
				textView.setText(session.Name);*/
			}
		}
		
		textView = (TextView)findViewById(R.id.speaker_details_name);
		textView.setText(_speaker.Name);
		
		textView = (TextView)findViewById(R.id.speaker_details_bio);
		textView.setText(_speaker.Bio);
		
		Linkify.addLinks(textView, Linkify.EMAIL_ADDRESSES | Linkify.WEB_URLS);
		
		final ImageView imageView = (ImageView)findViewById(R.id.speaker_details_picture);		

		Bitmap image = App.GetImageFromCache(Utilities.GetKey(_speaker.ImageUrl));
		if(image != null) {
			imageView.setImageBitmap(image);
		} else {
			new HttpGetImageAsyncTask(imageView).Get(Utilities.EncodeUrl(_speaker.ImageUrl).toString());
		}
	}
}
