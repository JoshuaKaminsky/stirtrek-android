package com.stirtrek.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class AlternatingListViewAdapter<T> extends BaseArrayAdapter<T>{

	private int[] colors = new int[] { 0x22222222, 0x44444444 };
	
	public AlternatingListViewAdapter(Context context, int layoutId, T[] objects) {
		super(context, layoutId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		
		int colorPos = position % colors.length;
		view.setBackgroundColor(colors[colorPos]);
		
		return view;		
	}
}
