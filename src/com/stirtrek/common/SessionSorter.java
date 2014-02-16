package com.stirtrek.common;

import java.util.Comparator;

import android.util.SparseArray;

import com.stirtrek.model.Session;
import com.stirtrek.model.TimeSlot;

public class SessionSorter implements Comparator<Session>{
	private SparseArray<TimeSlot> _timeslots = new SparseArray<TimeSlot>();
	
	public SessionSorter(TimeSlot[] timeSlots){
		for(TimeSlot timeslot : timeSlots){
			_timeslots.put(timeslot.Id, timeslot);
		}
	}
	
	public int compare(Session arg0, Session arg1) {
		if(arg0 == null)
			return 1;
		if(arg1 == null)
			return -1;				
		
		TimeSlot timeslot0 = _timeslots.get(arg0.TimeSlotId);
		TimeSlot timeslot1 = _timeslots.get(arg1.TimeSlotId);
			
		return new TimeSlotSorter().compare(timeslot0, timeslot1);			
	}
}
