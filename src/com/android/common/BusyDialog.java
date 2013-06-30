package com.android.common;

import stirtrek.activity.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BusyDialog {

	private AlertDialog _busyDialog;
	
	public BusyDialog(Context context) {
		Builder builder = new Builder(context);
		View layout = LayoutInflater.from(context).inflate(R.layout.busy_dialog, null);

		ProgressBar progressBar = (ProgressBar) layout.findViewById(R.id.busy_bar);
		progressBar.setIndeterminate(true);
		
		TextView textView = (TextView) layout.findViewById(R.id.busy_text);
		textView.setText("Loading");
		
		builder = new AlertDialog.Builder(context);
		builder.setView(layout);
		
		_busyDialog = builder.create();		
	}		
	
	public void SetText(String text) {
		TextView textView = (TextView) _busyDialog.findViewById(R.id.busy_text);
		textView.setText("Loading");
	}
	
	public void ShowBusy(boolean on) {
		if(on)
			_busyDialog.show();
		else
			_busyDialog.dismiss();
	}
}
