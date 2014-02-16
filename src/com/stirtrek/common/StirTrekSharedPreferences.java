package com.stirtrek.common;

import com.android.client.utilities.JsonUtilities;
import com.stirtrek.model.Response;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class StirTrekSharedPreferences {
	
	static final String PREFERENCE_NAME = "stirtrek_preferences_shared";
	static final String PREF_KEY_RESPONSE = "response";
	
	private final SharedPreferences _sharedPreferences;
    
	public StirTrekSharedPreferences(Context context) {
    	_sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0);	
	}
	
	public Response getStoredResponse() {
		try {
			String responseJson = _sharedPreferences.getString(PREF_KEY_RESPONSE, "");
			
			return JsonUtilities.parseJson(responseJson, Response.class);
		} catch(Exception ex) {
			return null;
		}			
	}
	
	public boolean setStoredResponse(Response response) {
		try {
			Editor sharedPreferenceEditor = _sharedPreferences.edit();
	    	
			String jsonResponse = JsonUtilities.getJson(response);
			
			sharedPreferenceEditor.putString(PREF_KEY_RESPONSE, jsonResponse);
	        
	        sharedPreferenceEditor.commit(); // save changes
		}
		catch(Exception ex) {
			Log.e("Error saving stirtrek response", ex.getMessage());
			return false;
		}
		
        return true;		
	}
}
