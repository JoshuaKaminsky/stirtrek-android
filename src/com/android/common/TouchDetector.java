package com.android.common;

import com.android.contract.ITouchListener;

import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

public class TouchDetector extends SimpleOnGestureListener {

	private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private ITouchListener _listener;

    public TouchDetector(ITouchListener listener) {
    	_listener = listener;
    }
	
	@Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;
            // right to left swipe
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                _listener.OnFlingGestureCaptured(FlingGesture.Right);
            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            	_listener.OnFlingGestureCaptured(FlingGesture.Left);
            }
        } catch (Exception e) {
            // nothing
        }
        return true;
    }
	
	@Override
	public boolean onDown(MotionEvent e) {		
		super.onDown(e);
		_listener.OnDown();
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		super.onLongPress(e);
		_listener.OnLongPress();
	}
}
