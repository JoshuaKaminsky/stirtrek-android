package com.android.client.utilities;

import java.lang.ref.WeakReference;

import android.graphics.drawable.AnimationDrawable;

public class DownloadingDrawable extends AnimationDrawable {
	
    private final WeakReference<HttpGetImageAsyncTask> _imageTaskReference;

    public DownloadingDrawable(HttpGetImageAsyncTask imageTask) {    	
        _imageTaskReference = new WeakReference<HttpGetImageAsyncTask>(imageTask);                
    }

    public HttpGetImageAsyncTask getImageDownloaderTask() {
        return _imageTaskReference.get();
    }
}
