package com.stirtrek.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

import com.android.common.BusyDialog;
import com.android.common.FlingGesture;
import com.android.common.TouchDetector;
import com.android.contract.ITouchListener;

public class BaseActivity extends Activity implements ITouchListener{
	private List<ITouchListener> _touchListeners = new ArrayList<ITouchListener>();	
    
	private BusyDialog _busyDialog;
	private int _layoutResource;
	private OnTouchListener _onTouchListener;

	public BaseActivity(int layoutResource)
	{
		_layoutResource = layoutResource;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		RelativeLayout relativeLayout = new RelativeLayout(this);
		relativeLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		View view = LayoutInflater.from(this).inflate(_layoutResource, null);
		
		_busyDialog = new BusyDialog(this);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		
		relativeLayout.addView(view);
		
		final GestureDetector gestureDetector = new GestureDetector(new TouchDetector(this));	
		_onTouchListener = new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		};
		
		//relativeLayout.setOnTouchListener(_onTouchListener);      				
		
		setContentView(relativeLayout);			
	}

	protected void SetBusy(boolean isBusy)
	{
		_busyDialog.ShowBusy(isBusy);
	}	

	public View.OnTouchListener GetOnTouchListener() {
		return _onTouchListener;
	}
	
	
	public void SetOnFlingGestureCaptured(ITouchListener listener)
	{
		_touchListeners.add(listener);
	}
	
	public void OnFlingGestureCaptured(FlingGesture gesture) {					
		for(ITouchListener listener : _touchListeners)
		{
			listener.OnFlingGestureCaptured(gesture);
		}		
	}

	public void OnDown() {
		// TODO Auto-generated method stub
		
	}

	public void OnLongPress() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {				
		return false;
	}
}
