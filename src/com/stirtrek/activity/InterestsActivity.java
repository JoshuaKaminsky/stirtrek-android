package com.stirtrek.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import stirtrek.activity.R;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.common.SeparatedListAdapter;
import com.stirtrek.adapter.InterestAdapter;
import com.stirtrek.adapter.SessionQuickDetails;
import com.stirtrek.application.StirTrek.App;
import com.stirtrek.common.TimeSlotSorter;
import com.stirtrek.model.Interest.Interests;
import com.stirtrek.model.Response;
import com.stirtrek.model.Session;
import com.stirtrek.model.TimeSlot;

public class InterestsActivity extends BaseActivity implements OnItemClickListener{
	
	Response _data;
	
	public InterestsActivity() {
		super(R.layout.interests);				
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Refresh();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Refresh();
	}
	
	private void Refresh(){
		_data = App.GetResponse();		
		
		if(_data == null)
			return;
		
		SparseArray<ArrayList<Session>> map = new SparseArray<ArrayList<Session>>();
		ArrayList<Session> sessions = new ArrayList<Session>(App.GetAllInterests());
		for(Session session : _data.Sessions){
			if(session.TrackId == null){
				//this is a global event
				sessions.add(session);
			}				
		}
		
		for(Session session : sessions){
			if(map.indexOfKey(session.TimeSlotId) >= 0){
				map.get(session.TimeSlotId).add(session);
			}
			else{
				map.put(session.TimeSlotId, new ArrayList<Session>(Arrays.asList(new Session[]{session})));
			}				
		}
		
		SeparatedListAdapter adapter = new SeparatedListAdapter(R.layout.interests_list_header, this);
		
		TimeSlot[] timeslots = _data.TimeSlots;		
		Arrays.sort(timeslots, new TimeSlotSorter());		
		for(TimeSlot timeslot : timeslots){
			List<Session> sessionList = map.get(timeslot.Id);
			
			if(sessionList == null){
				ArrayAdapter<String> emptyAdapter = new ArrayAdapter<String>(this,R.layout.interest_empty_list_item);

				emptyAdapter.add("** click to view sessions **");		
				
				adapter.addSection(timeslot.GetId(), timeslot.GetName(), emptyAdapter);								
				
				continue;
			}
			else{
				if(sessionList.get(0).TrackId == null){
					ArrayAdapter<String> generalAdapter = new ArrayAdapter<String>(this,R.layout.interest_general_list_item);
					
					for (Session session : sessionList) {
						generalAdapter.add(session.Name);
					}					
					
					adapter.addSection(timeslot.GetId(), timeslot.GetName(), generalAdapter);
					continue;
				}
			}			
			
			InterestAdapter interestAdapter = new InterestAdapter(
					this,  
					sessionList, 
					Arrays.asList(_data.Tracks));						
			
			adapter.addSection(timeslot.GetId(), timeslot.GetName(), interestAdapter);
		}
		
		ListView listView = (ListView)findViewById(R.id.interests_listView);		
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(this);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		SeparatedListAdapter listAdapter = (SeparatedListAdapter)parent.getAdapter();
		Class<? extends Adapter> clazz = listAdapter.getAdapterType(position);
		if(clazz == InterestAdapter.class) {
			showDetails((Session) listAdapter.getItem(position), view.getContext());
		}
		else if(clazz == ArrayAdapter.class) {
			if(view.getId() == R.id.interest_empty_list_item) {
				TextView textView = (TextView) view;
				if(textView != null) {
					TextView headerView = (TextView) listAdapter.getHeaderView(position, null, parent);
					Integer timeSlotId = listAdapter.getSectionId(headerView.getText().toString());
					
					//showTimeSlotOptions(timeSlotId, view.getContext());
				}
			}
			
		}
	}
	
	private void showDetails(final Session session, Context context) {
		final Dialog details = new Dialog(context);
		details.requestWindowFeature(Window.FEATURE_NO_TITLE);
		details.setCanceledOnTouchOutside(true);
		details.setCancelable(true);
		details.setContentView(R.layout.session_details);
		
		TextView textView = (TextView) details.findViewById(R.id.session_details_title);
		textView.setText(session.Name);
		
		textView = (TextView) details.findViewById(R.id.session_details_abstract);
		textView.setText(session.Abstract);
		
		LinearLayout linearLayout = (LinearLayout)details.findViewById(R.id.session_remove_layout);
		linearLayout.setVisibility(View.VISIBLE);
		linearLayout.setClickable(true);
		linearLayout.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
				String where = Interests.SESSIONID + " = ?";
	        	String[] args = new String[]{ Integer.toString(session.Id) };
	        	ContentResolver resolver = getContentResolver();
	            if(resolver.delete(Interests.CONTENT_URI, where, args) == 1)
	            	App.RefreshInterests(resolver);	
	            
	            Refresh();
	            
	            details.dismiss();
			}
		});
		
		details.show();
	}
	
	private void showTimeSlotOptions(int timeSlotId, Context context) {
		final Dialog details = new Dialog(context);
		details.requestWindowFeature(Window.FEATURE_NO_TITLE);
		details.setCanceledOnTouchOutside(true);
		details.setCancelable(true);
		details.setContentView(R.layout.session_quick_details);
		
		ArrayList<Session> sessions = new ArrayList<Session>();
		for (Session session : _data.Sessions) {
			if(session.TimeSlotId == timeSlotId)
				sessions.add(session);
		}
		
		ListView listView = (ListView)details.findViewById(R.id.session_quick_list_view);
		SessionQuickDetails quickDetails = 
				new SessionQuickDetails(context, sessions, Arrays.asList(_data.Tracks), Arrays.asList(_data.Speakers));
		listView.setAdapter(quickDetails);
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
				Session session = (Session)parent.getAdapter().getItem(position);
				ContentResolver resolver = view.getContext().getContentResolver();

				// add to the db				
				ContentValues values = new ContentValues();
				values.put(Interests.SESSIONID, session.Id);
				values.put(Interests.TIMESLOTID, session.TimeSlotId);
				resolver.insert(Interests.CONTENT_URI, values);
				
				Refresh();
				
				details.dismiss();
			}
		});
		
		details.show();
	}
}