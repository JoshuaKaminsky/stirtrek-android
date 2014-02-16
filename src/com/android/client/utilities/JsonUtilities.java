package com.android.client.utilities;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import com.google.gson.Gson;

public class JsonUtilities{
	
	public static <T> T parseJson(InputStream inputStream, Class<T> type) {
		if(inputStream == null)
			return null;		
		
		return new Gson().fromJson(new InputStreamReader(inputStream), type);
	}
	
	public static <T> T parseJson(String input, Class<T> type)
	{
		if(input == null)
			return null;
		
		Gson gson = new Gson();
		
		Reader reader = new StringReader(input);
		
		return gson.fromJson(reader, type);
	}
	
	public static <T extends Object> String getJson(T input)  {
		if(input == null)
			return "";
		
		Gson gson = new Gson();
		
		return gson.toJson(input);
	}
}
