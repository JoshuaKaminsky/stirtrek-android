package com.android.client.utilities;

import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Utilities {	
	
	public Bitmap getBitmap(String bitmapUrl) 
	{
		try 
		{
			URL url = new URL(bitmapUrl);
		    return BitmapFactory.decodeStream(url.openConnection().getInputStream());
		}
		catch(Exception ex) 
		{
			return null;
		}
	}
}
