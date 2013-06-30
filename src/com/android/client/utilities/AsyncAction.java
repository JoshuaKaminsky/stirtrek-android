package com.android.client.utilities;

import android.os.AsyncTask;

public class AsyncAction<T> extends AsyncTask<Void, Void, T>{

	IAsyncAction<T> _asyncAction;
	IResultCallback<T> _callback;
	
	public AsyncAction(IAsyncAction<T> action, IResultCallback<T> callback){
		_asyncAction = action;
		_callback = callback;
	}
	
	@Override
	protected T doInBackground(Void... params) {
		if(_asyncAction == null)
			return null;
		
		return _asyncAction.Execute(); 
	}		
	
	protected void onPostExecute(T result) {
		if(_callback == null)
			return;
		
		_callback.Callback(result);
	};
}
