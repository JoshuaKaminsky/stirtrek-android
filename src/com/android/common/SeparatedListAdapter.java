package com.android.common;

import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

public class SeparatedListAdapter extends BaseAdapter
{
	public final Map<String, Adapter> _sections = new LinkedHashMap<String, Adapter>();
	public final ArrayAdapter<String> _headers;
	public final static int TYPE_SECTION_HEADER = 0;

	public SeparatedListAdapter(ArrayAdapter<String> headers, Context context)
	{
		_headers = headers; //new ArrayAdapter<String>(context, R.layout.interests_list_header);
	}

	public void addSection(String section, Adapter adapter)
	{
		this._headers.add(section);
		this._sections.put(section, adapter);
	}

	public Object getItem(int position)
	{
		for (Object section : this._sections.keySet())
		{
			Adapter adapter = _sections.get(section);
			int size = adapter.getCount() + 1;
			
			// check if position inside this section
			if (position == 0) return section;
			if (position < size) return adapter.getItem(position - 1);
			
			// otherwise jump into next section
			position -= size;
		}
		return null;
	}
	
	public int getCount()
	{
		// total together all sections, plus one for each section header
		int total = 0;
		for (Adapter adapter : this._sections.values())
			total += adapter.getCount() + 1;
		return total;
	}
	
	@Override
	public int getViewTypeCount()
	{
		// assume that headers count as one, then total all sections
		int total = 1;
		for (Adapter adapter : this._sections.values())
			total += adapter.getViewTypeCount();
		return total;
	}
	
	@Override
	public int getItemViewType(int position)
	{
		int type = 1;
		for (Object section : this._sections.keySet())
		{
			Adapter adapter = _sections.get(section);
			int size = adapter.getCount() + 1;
			
			// check if position inside this section
			if (position == 0) return TYPE_SECTION_HEADER;
			if (position < size) return type + adapter.getItemViewType(position - 1);
			
			// otherwise jump into next section
			position -= size;
			type += adapter.getViewTypeCount();
		}
		return -1;
	}
	
	public boolean areAllItemsSelectable()
	{
		return false;
	}
	
	@Override
	public boolean isEnabled(int position)
	{
		return (getItemViewType(position) != TYPE_SECTION_HEADER);
	}
	
	public View getView(int position, View convertView, ViewGroup parent)
	{
		int sectionnum = 0;
		for (Object section : this._sections.keySet())
		{
			Adapter adapter = _sections.get(section);
			int size = adapter.getCount() + 1;
			
			// check if position inside this section
			if (position == 0) return _headers.getView(sectionnum, convertView, parent);
			if (position < size) return adapter.getView(position - 1, convertView, parent);
			
			// otherwise jump into next section
			position -= size;
			sectionnum++;
		}
		return null;
	}
	
	public long getItemId(int position)
	{
		return position;
	}
	
	public Class<? extends Adapter> getAdapterType(int position) {		
		for (Object section : this._sections.keySet())
		{
			Adapter adapter = _sections.get(section);
			int size = adapter.getCount() + 1;
			
			// check if position inside this section
			if (position == 0) return null;
			if (position < size) return adapter.getClass();
			
			// otherwise jump into next section
			position -= size;
		}
		return null;		
	}
}
		