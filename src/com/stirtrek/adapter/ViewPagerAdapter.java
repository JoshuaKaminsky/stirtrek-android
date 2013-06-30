package com.stirtrek.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import stirtrek.activity.R;
import com.stirtrek.common.TrackSorter;
import com.stirtrek.model.Response;
import com.stirtrek.model.Session;
import com.stirtrek.model.Speaker;
import com.stirtrek.model.TimeSlot;
import com.stirtrek.model.Track;
import android.app.Dialog;
import com.android.common.ITitleProvider;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
 
public class ViewPagerAdapter extends PagerAdapter implements ITitleProvider, OnItemClickListener{
    private final Context _context;
    private TimeSlot[] _timeSlots;
    private Track[] _tracks;
    private Session[] _sessions;
    private Speaker[] _speakers;
    
    public ViewPagerAdapter(Response response, Context context)
    {
    	Arrays.sort(response.Tracks, new TrackSorter());
    	
        this._context = context;
        this._timeSlots = response.TimeSlots;
        this._tracks = response.Tracks;
        this._sessions = response.Sessions;
        this._speakers = response.Speakers;
    }
    
    public String GetTitle(int position)
    {
    	if(position > getCount())
    		return "";
    	
        return _tracks[position].GetName();
    }
 
    @Override
    public int getCount()
    {
        return _tracks.length;
    }
 
    @Override
    public Object instantiateItem(View pager, int position)
    {   
    	LayoutInflater inflater = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	ListView schedule = (ListView)inflater.inflate(R.layout.schedule, null);
    	
    	Track selectedTrack =_tracks[position];    	    	
    	
    	List<Session> sessions = new ArrayList<Session>();
		for(Session session : _sessions)
		{
			if(session.TrackId == null)
				continue;
			
			if(session.TrackId == selectedTrack.Id)
			{
				sessions.add(session);
			}							
		}
		
		if(sessions.isEmpty())
			return null;
		
		schedule.setAdapter(
				new SessionAdapter(
						_context,
						sessions, 
						Arrays.asList(_timeSlots), 
						Arrays.asList(_speakers)));

		schedule.setClickable(true);
		schedule.setFocusable(true);
		schedule.setOnItemClickListener(this);		
		
		((ViewPager) pager).addView(schedule, 0);
		
        return schedule;
    }
 
    @Override
    public void destroyItem(View pager, int position, Object view)
    {
        ((ViewPager)pager).removeView((ListView)view);
    }
 
    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view.equals(object);
    }
 
    @Override
    public void finishUpdate(View view) {}
 
    @Override
    public void restoreState(Parcelable p, ClassLoader c) {}
 
    @Override
    public Parcelable saveState() {
        return null;
    }
 
    @Override
    public void startUpdate(View view) {}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
		Session session = (Session) parent.getAdapter().getItem(position);
		
		Dialog details = new Dialog(view.getContext());
		details.requestWindowFeature(Window.FEATURE_NO_TITLE);
		details.setCanceledOnTouchOutside(true);
		details.setCancelable(true);
		details.setContentView(R.layout.session_details);
		
		TextView textView = (TextView) details.findViewById(R.id.session_details_title);
		textView.setText(session.Name);
		
		textView = (TextView) details.findViewById(R.id.session_details_abstract);
		textView.setText(session.Abstract);
		
		details.show();
	}
}