package com.stirtrek.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.stirtrek.model.Interest;
import com.stirtrek.model.Response;
import com.stirtrek.model.Session;
import com.stirtrek.model.Interest.Interests;
import com.android.client.utilities.CollectionUtilities;
import com.android.client.utilities.HttpGetAsyncTask;
import com.android.client.utilities.IResultCallback;
import android.content.ContentResolver;
import android.database.Cursor;

public final class StirTrek {
	public static String PreferencesFile = "StirTrekPreferences";
	
	public static class App extends android.app.Application {
		private static final String stirtrekJsonUrl = "http://stirtrek.com/Feed/JSON";
		
		private static ArrayList<Interest> _interests = new ArrayList<Interest>();
		private static Response _response;		
		private static List<IResponseListener> _listeners = new ArrayList<IResponseListener>();
		
		public App() {}
		
		@Override
		public void onCreate() {
			RefreshResponse();			
			RefreshInterests(getContentResolver());
		}		
		
		public static void RefreshInterests(ContentResolver resolver) {
			_interests.clear();
			
			String[] projection = new String[4];
			Interests.sInterestsProjectionMap.values().toArray(projection);
			Cursor cursor = resolver.query(Interests.CONTENT_URI, projection, null, null, null);
			
			if(cursor != null) {						
				cursor.moveToFirst();
				while(!cursor.isAfterLast()) {
					int sessionId = cursor.getInt(cursor.getColumnIndex(Interests.SESSIONID));
					int timeslotId = cursor.getInt(cursor.getColumnIndex(Interests.TIMESLOTID));
					
					Interest interest = new Interest();
					interest.SessionId = sessionId;
					interest.TimeSlotId = timeslotId;
					
					_interests.add(interest);
					
					cursor.moveToNext();
				}						
			}	
			
			cursor.close();
		}

		public static void SetOnResponseReceived(IResponseListener listener)
		{
			if(!_listeners.contains(listener))
				_listeners.add(listener);
		}
		
		public static boolean IsInterest(int sessionId) {
			for(Interest interest : _interests) {
				if(interest.SessionId == sessionId)
					return true;
			}
			
			return false;
		}		
		
		public static List<Session> GetAllInterests(){
			if(GetResponse() == null){
				return null;
			}
			
			List<Session> interests = new ArrayList<Session>(); 			
			List<Session> sessions = Arrays.asList(_response.Sessions);
			
			for(Interest interest : _interests) {					
				Session session = CollectionUtilities.GetItem(interest.SessionId, sessions);
					
				if(session != null)
					interests.add(session);
			}
			
			return interests;
		}
		
		public static List<Session> GetInterest(int timeslotId) {
			List<Session> interests = new ArrayList<Session>(); 			
			List<Session> sessions = Arrays.asList(_response.Sessions);
			
			for(Interest interest : _interests) {
				if(interest.TimeSlotId == timeslotId) {
					
					Session session = CollectionUtilities.GetItem(interest.SessionId, sessions);
					
					if(session != null)
						interests.add(session);
				}
			}
			
			return interests;
		}
		
		public static Response GetResponse() {
			if(_response == null)
				RefreshResponse();
			
			return _response;
		}
		
		public static void RefreshResponse()
		{
			new HttpGetAsyncTask<Response>(new ResultCallback()).execute(stirtrekJsonUrl);
		}
		
		private static class ResultCallback implements IResultCallback<Response> {

			public void Callback(Response result) {
				_response = result;
				
				for(IResponseListener listener : _listeners)
				{
					listener.OnResponseReceived(_response);
				}				
			}

			public Class<Response> GetType() {
				return Response.class;
			}
			
		}
	}	
}
