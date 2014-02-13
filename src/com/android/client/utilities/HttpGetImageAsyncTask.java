package com.android.client.utilities;

import java.io.InputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.stirtrek.application.StirTrek.App;

public class HttpGetImageAsyncTask extends  AsyncTask<String, Void, Bitmap>{
	
	private IResultCallback<Bitmap> _callback;	
	
	public HttpGetImageAsyncTask(IResultCallback<Bitmap> callback) {	
		_callback = callback;
	}	
	
	@Override
	protected Bitmap doInBackground(String... urls) {
		//currently only supports one url call at a time
		if(urls.length != 1)
			return null;
		
		URL url = Utilities.EncodeUrl(urls[0]);	
		
		Bitmap bitmap = App.GetImageFromCache(Utilities.GetKey(urls[0]));

        if (bitmap == null) {		
        	InputStream content = HttpClientUtilities.RetrieveStreamContent(url.toString());	
        	
        	bitmap = BitmapFactory.decodeStream(content);
        }
        
        return bitmap;
	}
		
	@Override	
	protected void onPostExecute(Bitmap image) {
		if(_callback != null)
		{
			_callback.Callback(image);
		}		
	}		
}
	
	

