package com.stirtrek.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;

import com.stirtrek.model.TimeSlot;

public class TimeSlotSorter implements Comparator<TimeSlot>{

	public int compare(TimeSlot arg0, TimeSlot arg1) {
		if(arg0 == null && arg1 == null)
			return 0;
		if(arg0 == null)
			return 1;
		if(arg1 == null)
			return -1;
		

		DateFormat format0;
		DateFormat format1;
		
		try
		{
			TimeSlot timeslot0 = arg0;
			TimeSlot timeslot1 = arg1;
			
			if(timeslot0.StartTime.length() == 7)
				format0 = new SimpleDateFormat("h:mm aa");
			else
				format0 = new SimpleDateFormat("hh:mm aa");
			
			if(timeslot1.StartTime.length() == 7)
				format1 = new SimpleDateFormat("h:mm aa");
			else
				format1 = new SimpleDateFormat("hh:mm aa");
		
			return format0.parse(timeslot0.StartTime).compareTo(format1.parse(timeslot1.StartTime));
		}
		catch(Exception e)
		{
			return 0;
		}
	}
}
