package com.stirtrek.application;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.android.client.utilities.CollectionUtilities;
import com.android.client.utilities.HttpAsyncTask;
import com.android.client.utilities.HttpGetImageAsyncTask;
import com.android.client.utilities.JsonUtilities;
import com.android.client.utilities.Utilities;
import com.android.common.ImageCache;
import com.android.contract.IResultCallback;
import com.stirtrek.contract.IResponseListener;
import com.stirtrek.model.Interest;
import com.stirtrek.model.Interest.Interests;
import com.stirtrek.model.Response;
import com.stirtrek.model.Session;
import com.stirtrek.model.Speaker;

public final class StirTrek {
	public static String PreferencesFile = "StirTrekPreferences";
	
	public static class App extends android.app.Application {
		private static final String stirtrekJsonUrl = "http://stirtrek.com/Feed/JSON";		
		
		private static final String appPreferenceKey = "stirtrek_preferences_key";
		
		private static Response _response;		
		private static ArrayList<IResponseListener> _listeners = new ArrayList<IResponseListener>();
		private static ArrayList<Interest> _interests = new ArrayList<Interest>();
		
		private static ImageCache _imageCache;
		
		private static Context context;

	    public static void initialize(Context context){
	        App.context = context;
	        
	        _response = getData();
	    }
		
	    public static Context getAppContext() {
	        return App.context;
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
		
		public static void SetResponseListener(IResponseListener listener)
		{
			if(!_listeners.contains(listener))
				_listeners.add(listener);
		}
		
		public static void RemoveResponseListener(IResponseListener listener) {
			if(_listeners.contains(listener)) {
				_listeners.remove(listener);
			}
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
			Session[] sessions = _response.Sessions;
			
			for(Interest interest : _interests) {					
				Session session = CollectionUtilities.GetItem(interest.SessionId, sessions);
					
				if(session != null)
					interests.add(session);
			}
			
			return interests;
		}
		
		public static List<Session> GetInterest(int timeslotId) {
			List<Session> interests = new ArrayList<Session>(); 			
			Session[] sessions = _response.Sessions;
			
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
			return _response;
		}
		
		public static void RefreshResponse()
		{
			new HttpAsyncTask<Void, Response>(new ResultCallback()).Get(stirtrekJsonUrl);
		}
			
		public static Bitmap GetImageFromCache(String key) {
			return _imageCache.getBitmap(key);
		}
					
		private static class ResultCallback implements IResultCallback<Response> {

			public void Callback(Response result) {
				if(result == null || (_response != null && JsonUtilities.getJson(result) == JsonUtilities.getJson(_response))) {
					return;
				}
				
				storeData(result);
				
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
		
		private static void storeData(Response data) {
			SharedPreferences preferences = context.getSharedPreferences(appPreferenceKey, 0);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("stirtrek_data", JsonUtilities.getJson(data));
			editor.commit();
		}
		
		private static Response getData() {
			SharedPreferences preferences = context.getSharedPreferences(appPreferenceKey, 0);
			String jsonData = preferences.getString("stirtrek_data", "");
			if(jsonData == null || jsonData == "") {
				return null;
			}
			
			return JsonUtilities.parseJson(jsonData, Response.class);
		}
	}		
}
