package com.android.client.utilities;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.Bitmap;
import android.os.Handler;

public class ImageCache {

	private static final int HARD_CACHE_CAPACITY = 10;
    private static final int DELAY_BEFORE_PURGE = 10 * 1000; //10 seconds

    private final static HashMap<String, Bitmap> hardCache =
        new LinkedHashMap<String, Bitmap>(HARD_CACHE_CAPACITY / 2, 0.75f, true) {
    	
        private static final long serialVersionUID = -9175241457979514563L;

		@Override
        protected boolean removeEldestEntry(LinkedHashMap.Entry<String, Bitmap> eldest) {
            if (size() > HARD_CACHE_CAPACITY) {
            	softCache.put(eldest.getKey(), new SoftReference<Bitmap>(eldest.getValue()));
                return true;
            } else
                return false;
        }
    };

    private final static ConcurrentHashMap<String, SoftReference<Bitmap>> softCache =
        new ConcurrentHashMap<String, SoftReference<Bitmap>>(HARD_CACHE_CAPACITY / 2);

    private final Handler purgeHandler = new Handler();
    
    private final Runnable purger = new Runnable() {
        public void run() {
            clearCache();
        }
    };

    public void addBitmapToCache(String url, Bitmap bitmap) {
        if (bitmap != null) {
            synchronized (hardCache) {
            	hardCache.put(url, bitmap);
            }
        }
    }

    public Bitmap getBitmapFromCache(String url) {
    	resetPurgeTimer();
    	
        synchronized (hardCache) {
            final Bitmap bitmap = hardCache.get(url);
            if (bitmap != null) {
                hardCache.remove(url);
            	hardCache.put(url, bitmap);
                return bitmap;
            }
        }

        SoftReference<Bitmap> bitmapReference = softCache.get(url);
        if (bitmapReference != null) {
            final Bitmap bitmap = bitmapReference.get();
            if (bitmap != null) {
                return bitmap;
            } else {
            	softCache.remove(url);
            }
        }

        return null;
    }
 
    public void clearCache() {
        hardCache.clear();
        softCache.clear();
    }

    private void resetPurgeTimer() {
        purgeHandler.removeCallbacks(purger);
        purgeHandler.postDelayed(purger, DELAY_BEFORE_PURGE);
    }    
}
