package com.android.client.utilities;

import java.io.InputStream;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HttpGetImageAsyncTask extends HttpGetAsyncTask<Bitmap>{
	
	private IResultCallback<Bitmap> _callback;	
	
	public HttpGetImageAsyncTask(IResultCallback<Bitmap> callback) {
		super(callback);
		_callback = callback;
	}				
	
	@Override	
	protected void onPostExecute(InputStream inputStream) {
		if(_callback != null)
		{
			Bitmap result = BitmapFactory.decodeStream(inputStream);
			_callback.Callback(result);
		}		
	}		
}
	
	

