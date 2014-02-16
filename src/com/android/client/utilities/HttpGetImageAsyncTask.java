package com.android.client.utilities;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;

import org.apache.http.HttpEntity;

import com.android.contract.IResultCallback;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class HttpGetImageAsyncTask extends HttpAsyncTask<Void, Bitmap>{
	
	private static WeakReference<ImageView> _imageViewReference;
	private String url;
	
	public HttpGetImageAsyncTask(ImageView imageView) {
		super(new IResultCallback<Bitmap>() {

			public Class<Bitmap> GetType() {
				return Bitmap.class;
			}

			public void Callback(Bitmap result) {
				if (_imageViewReference != null) {
		            ImageView imageView = _imageViewReference.get();
		            if (imageView != null) {
		                imageView.setImageBitmap(result);
		            }
		        }
				
				return;	
			}
		});
		
		_imageViewReference = new WeakReference<ImageView>(imageView);
	}		
	
	public HttpGetImageAsyncTask(IResultCallback<Bitmap> callback) {
		super(callback);
	}		
	
	@Override
	public void Get(final String url) {
		_httpAction = new Callable<HttpEntity>() {

			public HttpEntity call() throws Exception {
				return HttpClientUtilities.get(url);				
			}			
		};
		
		execute("");		
	}
	
	@Override
	protected Bitmap doInBackground(String... urls) {
		InputStream inputStream = getDataStream();
		
		Bitmap result = null;
		
		if(!isCancelled()) {
			 result = BitmapFactory.decodeStream(inputStream);
		}
		
		return result;
	};
	
	@Override	
	protected void onPostExecute(Bitmap result) {
		if(_callback != null) {
			_callback.Callback(result);
		}		
	}

	public String getUrl() {
		return url;
	}		
}
	
	

