package com.android.common;

public interface ITouchListener {
	void OnFlingGestureCaptured(FlingGesture gesture);
	
	void OnDown();
	
	void OnLongPress();
}
