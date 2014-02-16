package com.stirtrek.activity;

import java.util.Arrays;

import stirtrek.activity.R;
import android.os.Bundle;
import android.widget.ListView;

import com.stirtrek.adapter.SpeakerAdapter;
import com.stirtrek.application.StirTrek.App;

public class SpeakerActivity extends BaseActivity {

	public SpeakerActivity() {
		super(R.layout.speaker);				
	}	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		
		if(App.GetResponse() == null){
			return;
		}
		
		ListView listView = (ListView)findViewById(R.id.speakers_listView);
		listView.setAdapter(new SpeakerAdapter(getBaseContext(), Arrays.asList(App.GetResponse().Speakers)));		
	}
}
