package com.stirtrek.model;

import com.android.common.model.IIdentifier;
import com.android.common.model.INamed;

public class TimeSlot implements IIdentifier, INamed{
	
	public int Id;
    
	public String StartTime;
    
	public String EndTime;
	
	public int GetId() {
		return Id;
	}

	public String GetName() {
		return StartTime;
	}
}
