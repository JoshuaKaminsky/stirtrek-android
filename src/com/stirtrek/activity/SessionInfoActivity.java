package com.stirtrek.activity;

import stirtrek.activity.R;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.android.client.utilities.HttpGetImageAsyncTask;
import com.android.client.utilities.JsonUtilities;
import com.android.client.utilities.Utilities;
import com.stirtrek.application.StirTrek.App;
import com.stirtrek.model.Session;
import com.stirtrek.model.Speaker;
import com.stirtrek.model.Interest.Interests;

public class SessionInfoActivity extends BaseActivity {

	private Session _session;
	private Speaker _speaker;
	
	public SessionInfoActivity() {
		super(R.layout.session_details);
		
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String sessionData = getIntent().getExtras().getString("SessionData");
		String speakerData = getIntent().getExtras().getString("SpeakerData");
		
		_session = JsonUtilities.parseJson(sessionData, Session.class);
		_speaker = JsonUtilities.parseJson(speakerData, Speaker.class);
		
		Refresh();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Refresh();
	}
	
	private void Refresh() {
		TextView textView = (TextView)findViewById(R.id.session_details_title);
		textView.setText(_session.Name);
		
		textView = (TextView)findViewById(R.id.session_details_abstract);
		textView.setText(_session.Abstract);
		
		textView = (TextView)findViewById(R.id.speaker_details_name);
		textView.setText(_speaker.Name);
		
		Linkify.addLinks(textView, Linkify.EMAIL_ADDRESSES | Linkify.WEB_URLS);
		
		final ImageView imageView = (ImageView)findViewById(R.id.speaker_details_picture);		

		Bitmap image = App.GetImageFromCache(Utilities.GetKey(_speaker.ImageUrl));
		if(image != null) {
			imageView.setImageBitmap(image);
		} else {
			new HttpGetImageAsyncTask(imageView).Get(Utilities.EncodeUrl(_speaker.ImageUrl).toString());
		}
		
		ToggleButton favorite = (ToggleButton) findViewById(R.id.session_star);
		favorite.setChecked(App.IsInterest(_session.Id));
		favorite.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				ContentResolver resolver = getBaseContext().getContentResolver();

				if (isChecked) {
					// add to the db
					ContentValues values = new ContentValues();
					values.put(Interests.SESSIONID, _session.Id);
					values.put(Interests.TIMESLOTID, _session.TimeSlotId);
					resolver.insert(Interests.CONTENT_URI, values);

				} else {
					// remove from the db
					String where = Interests.SESSIONID + " = ?";
					String[] args = new String[] { Integer.toString(_session.Id) };
					resolver.delete(Interests.CONTENT_URI, where, args);
					App.RefreshInterests(resolver);
				}

				App.RefreshInterests(resolver);
			}
		});
	}
}
