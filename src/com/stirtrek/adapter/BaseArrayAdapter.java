package com.stirtrek.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class BaseArrayAdapter<T> extends ArrayAdapter<T>{

	private int _layoutId;
	protected LayoutInflater _inflater;
	private int _count;		
	
	public BaseArrayAdapter(Context context, int layoutId, List<T> objects) {
		super(context, layoutId, objects);

		if(objects == null)
			_count = 0;
		else
			_count = objects.size();
		
		_layoutId = layoutId;
		_inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView != null)
			return convertView;
		
		return _inflater.inflate(_layoutId, null);		
	}
	
	@Override
	public int getCount() {
		return _count;
	}
}
