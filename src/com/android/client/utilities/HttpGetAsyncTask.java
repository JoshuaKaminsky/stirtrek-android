package com.android.client.utilities;

import java.io.InputStream;

import android.os.AsyncTask;

public class HttpGetAsyncTask<T> extends AsyncTask<String, Void, InputStream>{
	
	private IResultCallback<T> _callback;
			
	public HttpGetAsyncTask(IResultCallback<T> callback)
	{
		_callback = callback;
	}	

	@Override
	protected InputStream doInBackground(String... urls) {
		//currently only supports one url call at a time
		if(urls.length != 1)
			return null;
		
		return HttpClientUtilities.RetrieveStream(urls[0]);			
	}
	
	@Override
	protected void onPostExecute(InputStream inputStream) {
		super.onPostExecute(inputStream);
		
		if(_callback != null)
		{
			T result = JsonUtilities.ParseJson(inputStream, _callback.GetType());
			_callback.Callback(result);
		}		
	}	
}
