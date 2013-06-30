package com.android.client.utilities;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.google.gson.Gson;

public class JsonUtilities{
	
	/**
	 * @param <T>
	 * @param inputStream - the json stream
	 * @param type - the type of model to pull from json stream - must do this for android
	 * @return - the model objects
	 */
	public static <T> T ParseJson(InputStream inputStream, Class<T> type)
	{
		if(inputStream == null)
			return null;
		
		Gson gson = new Gson();
		
		Reader reader = new InputStreamReader(inputStream);
		
		return gson.fromJson(reader, type);
	}
}
