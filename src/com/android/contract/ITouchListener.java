package com.android.contract;

import com.android.common.FlingGesture;

public interface ITouchListener {
	void OnFlingGestureCaptured(FlingGesture gesture);
	
	void OnDown();
	
	void OnLongPress();
}
