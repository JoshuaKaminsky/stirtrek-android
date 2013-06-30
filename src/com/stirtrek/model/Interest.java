package com.stirtrek.model;

import java.util.HashMap;

import com.android.common.model.IIdentifier;

import android.content.UriMatcher;
import android.net.Uri;

public class Interest implements IIdentifier{
	public static final String AUTHORITY = "stirtrek.data.StirTrekDataProvider"; 

	public int Id;
	
	public int SessionId;

	public int TimeSlotId;
	
	public int GetId() {
		return Id;
	}

	public static final class Interests {
		private Interests(){}
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/interests");
		public static final String ID = "id";
		public static final String SESSIONID = "sessionid";
		public static final String TIMESLOTID = "timeslotid";
		public static final String CREATED_ON = "create_on";
		
		public static HashMap<String, String> sInterestsProjectionMap;	    
		private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		
		static {
			sURIMatcher.addURI(AUTHORITY, "interests", 0);
			
			sInterestsProjectionMap = new HashMap<String, String>();
			sInterestsProjectionMap.put(Interests.ID, Interests.ID);
			sInterestsProjectionMap.put(Interests.SESSIONID, Interests.SESSIONID);
			sInterestsProjectionMap.put(Interests.TIMESLOTID, Interests.TIMESLOTID);
			sInterestsProjectionMap.put(Interests.CREATED_ON, Interests.CREATED_ON);
		}
	}
}
