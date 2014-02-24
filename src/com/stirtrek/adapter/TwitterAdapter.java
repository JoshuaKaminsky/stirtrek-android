package com.stirtrek.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.twitter.model.Status;

import stirtrek.activity.R;
import android.content.Context;
import android.text.format.Time;
import android.text.util.Linkify;
import android.text.util.Linkify.TransformFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TwitterAdapter extends BaseArrayAdapter<Status>{
	
	public TwitterAdapter(Context context, Status[] objects) {
		super(context, R.layout.twitter_list_item, objects);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		
		Status result = getItem(position);
		
		TextView textView = (TextView)view.findViewById(R.id.twitter_text);
		textView.setText(result.text);
		
		Linkify.addLinks(textView, Linkify.ALL);
		
		textView = (TextView)view.findViewById(R.id.twitter_handle);
		textView.setText("- @" + result.user.handle + GetDateString(result.createdAt));
		
		// A transform filter that simply returns just the text captured by the
	    // first regular expression group.
	    TransformFilter mentionFilter = new TransformFilter() {
	        public final String transformUrl(final Matcher match, String url) {
	            return match.group(1);
	        }
	    };
		
		// Match @mentions and capture just the username portion of the text.
	    Pattern pattern = Pattern.compile("@([A-Za-z0-9_-]+)");
	    String scheme = "http://twitter.com/";
	    Linkify.addLinks(textView, pattern, scheme, null, mentionFilter);
		
		return view;
	}
	
	private String GetDateString(String dateTime)
	{
		LocalDateTime time = null;
		try
		{
			  SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH);
			  formatter.setLenient(true);
			  
			  time = new DateTime(formatter.parse(dateTime)).toLocalDateTime();
		}
		catch(Exception e)
		{
			return "";
		}
		
		Time nowTime = new Time();
		nowTime.setToNow();
		LocalDateTime now = new LocalDateTime(nowTime.toMillis(false));
		
		
		if(time.year().get() == now.year().get() 
			&& time.monthOfYear().get() == now.monthOfYear().get()
			&& time.dayOfMonth().get() == now.dayOfMonth().get())
		{
			return time.toString(" @ hh:mm aa");
		}
		
		return time.toString(" 'on' MM/dd/yyyy");
	}
}
