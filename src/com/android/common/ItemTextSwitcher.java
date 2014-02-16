package com.android.common;

import java.util.ArrayList;
import java.util.List;

import com.android.contract.IItemSwitchedListener;
import com.android.contract.INamed;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class ItemTextSwitcher<T extends INamed> extends ViewSwitcher {
	   
	private List<IItemSwitchedListener<T>> _listeners = new ArrayList<IItemSwitchedListener<T>>();
	private List<T> _items;
	private int _currentIndex = 0;
	
	public ItemTextSwitcher(List<T> items, Context context) {
		super(context);		
	
		_items = items;

		//load up the first view
		if(items.size() > 1)
		{
			addView(MakeView(items.get(0)));
			addView(MakeView(items.get(1)));			
		}
		
		setInAnimation(context, android.R.anim.slide_in_left);
		setOutAnimation(context, android.R.anim.slide_out_right);
	}
	
	public T getItem() {
		return this._items.get(_currentIndex);
	}
	
	public void setOnItemSwitchedListener(IItemSwitchedListener<T> listener) {
		_listeners.add(listener);
	}
	
	@Override
	public void addView(View child) {
		if (!(child instanceof TextView))
		{
            throw new IllegalArgumentException("TextSwitcher children must be instances of TextView");
        }
		super.addView(child);
	}
		
	public void ShowNext() {
		View view = getNextView();
		if(view != null)
			removeView(view);
		
		int nextIndex = _currentIndex + 1;
		if(nextIndex > (_items.size() - 1))
		{
			nextIndex = 0;
		}
		
		addView(MakeView(_items.get(nextIndex)));
		
		showNext();
		
		_currentIndex = nextIndex;
		notifyListener();
	}
	
	public void ShowPrevious() {
		View view = getNextView();
		if(view != null)
			removeView(view);
		
		int nextIndex = _currentIndex - 1;
		if(nextIndex < 0)
		{
			nextIndex = _items.size() - 1;
		}
		
		addView(MakeView(_items.get(nextIndex)));
		
		showPrevious();
		
		_currentIndex = nextIndex;
		notifyListener();
	}

	public void Show()
	{
		notifyListener();
	}
	
	private void notifyListener() {
		for(IItemSwitchedListener<T> listener : _listeners)
		{
			listener.OnItemSwitched(getItem());
		}	
	}
	
	private TextView MakeView(T item) {
		TextView textView = new TextView(getContext());
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.FILL;
		textView.setLayoutParams(layoutParams);
		textView.setText(item.GetName());
		textView.setTextSize(17);
		textView.setPadding(10, 5, 10, 5);
		textView.setTextColor(Color.BLACK);
		textView.setTag(item);
		
		return textView;
	}
}
