package com.stirtrek.model;

import com.android.common.model.IIdentifier;

public class Speaker implements IIdentifier{
	public int Id;
	
	public String Name;

	public String Bio;
	
	public int GetId() {
		return Id;
	}
}
