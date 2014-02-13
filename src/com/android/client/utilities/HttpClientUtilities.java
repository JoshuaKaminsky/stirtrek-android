package com.android.client.utilities;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpClientUtilities {

	public static String RetrieveStream(String url)
	{
		HttpEntity entity = RetrieveHttpEntity(url);
		if(entity != null) {		
			try {
				return EntityUtils.toString(entity);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	

	public static InputStream RetrieveStreamContent(String url)
	{
		HttpEntity entity = RetrieveHttpEntity(url);
		if(entity != null) {		
			try {
				return entity.getContent();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}	

	private static HttpEntity RetrieveHttpEntity(String url) {
		DefaultHttpClient client = new DefaultHttpClient();		
		HttpGet getRequest = new HttpGet(url);
		
		HttpResponse getResponse;
		try {
			getResponse = client.execute(getRequest);
		} catch (Exception e) {			
			e.printStackTrace();
			return null;
		}
		
		final int statusCode = getResponse.getStatusLine().getStatusCode();
		
		if(statusCode != HttpStatus.SC_OK)
		{
			Log.w("HttpClientUtilities.RetrieveStream(String url)",
					"Error " + statusCode + " for URL " + url);
					return null;					
		}
							
		return getResponse.getEntity();
	}
}
