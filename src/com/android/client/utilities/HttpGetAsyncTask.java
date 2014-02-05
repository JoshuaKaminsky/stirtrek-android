package com.android.client.utilities;

import android.os.AsyncTask;

public class HttpGetAsyncTask<T> extends AsyncTask<String, Void, String>{
	
	private IResultCallback<T> _callback;
			
	public HttpGetAsyncTask(IResultCallback<T> callback)
	{
		_callback = callback;
	}	

	@Override
	protected String doInBackground(String... urls) {
		//currently only supports one url call at a time
		if(urls.length != 1)
			return null;
		
		return HttpClientUtilities.RetrieveStream(urls[0]);			
	}
	
	@Override
	protected void onPostExecute(String input) {
		super.onPostExecute(input);
				
		if(_callback != null)
		{
			_callback.Callback(JsonUtilities.ParseJson(input, _callback.GetType()));						
		}		
	}	
}
