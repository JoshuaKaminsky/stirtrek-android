package com.android.client.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HttpGetImageAsyncTask extends HttpGetAsyncTask<Bitmap>{
	
	private IResultCallback<Bitmap> _callback;	
	
	public HttpGetImageAsyncTask(IResultCallback<Bitmap> callback) {
		super(callback);
		
		_callback = callback;
	}				
	
	@Override	
	protected void onPostExecute(String input) {
		if(_callback != null)
		{
			Bitmap result = BitmapFactory.decodeByteArray(input.getBytes(), 0, input.length());
			
			_callback.Callback(result);
		}		
	}		
}
	
	

