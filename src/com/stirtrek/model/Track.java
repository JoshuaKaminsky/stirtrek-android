package com.stirtrek.model;

import com.android.common.model.IIdentifier;
import com.android.common.model.INamed;

public class Track implements IIdentifier, INamed{

	public Track(int id, String name) {
		Id = id;
		Name = name;
	}
	
	public Track(){
		
	}
	
	public int Id;
	
	public String Name;
	
	public int GetId() {
		return Id;
	}

	public String GetName() {
		return Name;
	}
}
