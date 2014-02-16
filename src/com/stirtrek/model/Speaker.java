package com.stirtrek.model;

import com.android.contract.IIdentifier;

public class Speaker implements IIdentifier{
	public int Id;
	
	public String Name;

	public String Bio;
	
	public String ImageUrl;
	
	public int GetId() {
		return Id;
	}
}
