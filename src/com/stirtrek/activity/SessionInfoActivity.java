package com.stirtrek.activity;

import com.android.client.utilities.JsonUtilities;
import com.stirtrek.model.Session;

import android.os.Bundle;
import stirtrek.activity.R;

public class SessionInfoActivity extends BaseActivity {

	private Session _session;
	
	public SessionInfoActivity() {
		super(R.layout.session_details);
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String sessionData = getIntent().getExtras().getString("SessionData");
		_session = JsonUtilities.ParseJson(sessionData, Session.class);
		
		Refresh();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Refresh();
	}
	
	private void Refresh() {
		
	}
}
