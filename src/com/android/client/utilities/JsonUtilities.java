package com.android.client.utilities;

import java.io.Reader;
import java.io.StringReader;

import com.google.gson.Gson;

public class JsonUtilities{
	
	/**
	 * @param <T>
	 * @param input - the json string
	 * @param type - the type of model to pull from json string - must do this for android
	 * @return - the model objects
	 */
	public static <T> T ParseJson(String input, Class<T> type)
	{
		if(input == null)
			return null;
		
		Gson gson = new Gson();
		
		Reader reader = new StringReader(input);
		
		return gson.fromJson(reader, type);
	}
	
	public static <T extends Object> String GetJson(T input)  {
		if(input == null)
			return "";
		
		Gson gson = new Gson();
		
		return gson.toJson(input);
	}
}
