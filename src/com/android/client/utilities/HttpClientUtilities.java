package com.android.client.utilities;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class HttpClientUtilities {

	public static InputStream RetrieveStream(String url)
	{
		DefaultHttpClient client = new DefaultHttpClient();		
		HttpGet getRequest = new HttpGet(url);
		
		try
		{
			HttpResponse getResponse = client.execute(getRequest);
			final int statusCode = getResponse.getStatusLine().getStatusCode();
			
			if(statusCode != HttpStatus.SC_OK)
			{
				Log.w("HttpClientUtilities.RetrieveStream(String url)",
						"Error " + statusCode + " for URL " + url);
						return null;					
			}
			
			HttpEntity getResponseEntity = getResponse.getEntity();
			return getResponseEntity.getContent();
		}
		catch (IOException e) 
		{
			getRequest.abort();
			Log.w("HttpClientUtilities.RetrieveStream(String url)",
					"Error for URL " + url, e);
		}
		
		return null;
	}	
}
