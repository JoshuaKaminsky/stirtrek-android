package com.stirtrek.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.android.client.utilities.CollectionUtilities;
import com.android.client.utilities.HttpGetAsyncTask;
import com.android.client.utilities.HttpGetImageAsyncTask;
import com.android.client.utilities.IResultCallback;
import com.android.client.utilities.Utilities;
import com.android.common.ImageCache;
import com.stirtrek.common.IResponseListener;
import com.stirtrek.model.Interest;
import com.stirtrek.model.Interest.Interests;
import com.stirtrek.model.Response;
import com.stirtrek.model.Session;
import com.stirtrek.model.Speaker;

public final class StirTrek {
	public static String PreferencesFile = "StirTrekPreferences";
	
	public static class App extends android.app.Application {
		private static final String stirtrekJsonUrl = "http://stirtrek.com/Feed/JSON";		
		
		private static Response _response;		
		private static ArrayList<IResponseListener> _listeners = new ArrayList<IResponseListener>();
		private static ArrayList<Interest> _interests = new ArrayList<Interest>();
		
		private static ImageCache _imageCache;
		
		public App() {			
		}
		
		@Override
		public void onCreate() {
			new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... params) {
					_imageCache = new ImageCache(getApplicationContext());
					
					return null;
				}								
			}.execute((Void)null);
			
			RefreshResponse();			
			RefreshInterests(getContentResolver());
		}		
		
		public static void RefreshCache(final Context context) {
			if(_imageCache == null) {
				new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						_imageCache = new ImageCache(context);
						
						return null;
					}								
				}.execute();
			}
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

		public static void RefreshSpeakerImages(Speaker[] speakers) {
			for (Speaker speaker : speakers) {
				final String key = Utilities.GetKey(speaker.ImageUrl);				
				
				new HttpGetImageAsyncTask(new IResultCallback<Bitmap>() {
					
					@Override
					public Class<Bitmap> GetType() {
						return Bitmap.class;
					}
					
					@Override
					public void Callback(Bitmap result) {
						_imageCache.put(key, result);
					}
				});
			}
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
			
		public static Bitmap GetImageFromCache(String key) {
			return _imageCache.getBitmap(key);
		}
		
		private static class ResultCallback implements IResultCallback<Response> {

			public void Callback(Response result) {
				_response = result;
				
				for(IResponseListener listener : _listeners)
				{
					listener.OnResponseReceived(_response);
				}
				
				RefreshSpeakerImages(result.Speakers);
			}

			public Class<Response> GetType() {
				return Response.class;
			}
			
		}
	}		
}
