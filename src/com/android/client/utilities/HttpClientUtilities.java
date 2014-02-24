package com.android.client.utilities;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class HttpClientUtilities {

	public static HttpEntity get(String url) {
		Log.i("HTTPClientUtilities :: Get", url.toString());
		
		HttpGet getRequest = new HttpGet(url);
		
		return httpRequest(getRequest);
	}	
	
	public static HttpEntity getJson(String url) {
		Log.i("HTTPClientUtilities :: Get", url.toString());
		
		HttpGet getRequest = new HttpGet(url);
		getRequest.setHeader("Accept", "application/json");
		
		return httpRequest(getRequest);
	}	

	public static HttpEntity getAuthorizedJson(String url, String authorization) {
		Log.i("HTTPClientUtilities :: GET", url.toString());
		
		HttpGet getRequest = new HttpGet(url);
		getRequest.setHeader("Accept", "application/json");
		setAuthorizationHeader(getRequest, authorization);
		
		return httpRequest(getRequest);
	}
	
	public static HttpEntity postJson(String url, String jsonObject) {
		Log.i("HTTPClientUtilities :: POST", url.toString());
		
		HttpUriRequest request = getPostRequest(url, jsonObject);
		if (request == null) return null;
		
		return httpRequest(request);
	}
	
	public static HttpEntity postAuthorizedJson(String url, String authorization, String jsonObject) {
		Log.i("HTTPClientUtilities :: POST", url.toString());
		
		HttpUriRequest request = getPostRequest(url, jsonObject);
		if (request == null) return null;
		
		setAuthorizationHeader(request, authorization);
		
		return httpRequest(request);
	}
	
	public static HttpEntity putJson(String url, String jsonObject) {
		Log.i("HTTPClientUtilities :: PUT", url.toString());
		
		HttpUriRequest request = getPutRequest(url, jsonObject);
		if (request == null) return null;
		
		return httpRequest(request);
	}
	
	public static HttpEntity putAuthorizedJson(String url, String authorization, String jsonObject) {
		Log.i("HTTPClientUtilities :: PUT", url.toString());
		
		HttpUriRequest request = getPutRequest(url, jsonObject);
		if (request == null) return null;
		
		setAuthorizationHeader(request, authorization);
		
		return httpRequest(request);
	}
	
	public static HttpEntity deleteJson(String url) {
		Log.i("HTTPClientUtilities :: Delete", url.toString());
		
		return httpRequest(new HttpDelete(url));
	}

	public static HttpEntity deleteAuthorizedJson(String url, String authorization) {
		Log.i("HTTPClientUtilities :: Delete", url.toString());
		
		HttpDelete deleteRequest = new HttpDelete(url);
		setAuthorizationHeader(deleteRequest, authorization);
		
		return httpRequest(deleteRequest);
	}
	
	public static void addHeader(HttpUriRequest request, String header, String value) {
		request.addHeader(header,  value);
	}
	
	public static HttpEntity httpRequest(HttpUriRequest request) {
		DefaultHttpClient client = new DefaultHttpClient();			
		
		try
		{
			HttpResponse response = client.execute(request);
			final int statusCode = response.getStatusLine().getStatusCode();
			
			if(statusCode != HttpStatus.SC_OK)
			{
				Log.w("HttpClientUtilities.RetrieveStream(String url)",
						"Error " + statusCode + " for URL " + request.getURI().toString());
						return null;					
			}
			
			return response.getEntity();
		}
		catch (IOException e) 
		{
			request.abort();
			Log.w("HttpClientUtilities.RetrieveStream(String url)",
					"Error for URL " + request.getURI().toString(), e);
		}
		
		return null;
	}
	
	private static HttpUriRequest getPostRequest(String url, String jsonObject) {
		HttpPost postRequest = new HttpPost(url);	
		
		try {
			postRequest.setHeader("Accept", "application/json");     
            postRequest.setHeader("Content-Type", "application/json");     
			postRequest.setEntity(new StringEntity(jsonObject));
		}
		catch(Exception ex) {
			Log.e("Could not set post body " + jsonObject, ex.getMessage());
			return null;
		}
		
		return postRequest;
	}
	
	private static HttpUriRequest getPutRequest(String url, String jsonObject) {
		HttpPut putRequest = new HttpPut(url);
		
		try {
			putRequest.setHeader("Accept", "application/json");
            putRequest.setHeader("Content-Type", "application/json");
            putRequest.setEntity(new StringEntity(jsonObject));
		}
		catch(Exception ex) {
			Log.e("Could not set put body " + jsonObject, ex.getMessage());
			return null;
		}
		
		return putRequest;
	}
	
	private static void setAuthorizationHeader(HttpUriRequest request, String authorization) {
		request.setHeader("Authorization", authorization);		
	}
}
