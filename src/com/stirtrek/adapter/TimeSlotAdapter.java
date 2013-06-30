package com.stirtrek.adapter;

import com.stirtrek.model.TimeSlot;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


public class TimeSlotAdapter extends ArrayAdapter<TimeSlot> 
{

	public TimeSlotAdapter(Context context, int textViewResourceId, TimeSlot[] objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return super.getView(position, convertView, parent);
	}
}
