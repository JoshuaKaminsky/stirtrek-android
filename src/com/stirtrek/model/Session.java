package com.stirtrek.model;

import java.util.List;

import com.android.common.model.IIdentifier;

public class Session implements IIdentifier{

	public int Id;
	
	public String Name;
	
	public String Abstract;
	
	public List<Integer> SpeakerIds;
	
	public int TimeSlotId;
	
	public Integer TrackId;
	
	public List<String> Tags;

	public int GetId() {
		return Id;
	}
}
