package com.android.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;

import com.android.client.utilities.Utilities;
import com.jakewharton.disklrucache.DiskLruCache;

public class ImageCache {
	 
	private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
	private static final int MEMORY_CACHE_SIZE = 1024 * 1024; // 1MB
	private static final String DISK_CACHE_SUBDIR = "stirtrek/thumbnails";
	private static final int APP_VERSION = 1;
    private static final int VALUE_COUNT = 1; 

    private DiskLruCache _diskCache;
    
	public static LruCache<String, Bitmap> _memoryCache;	
	
    public ImageCache(Context context) {
        try {
        	_memoryCache = new LruCache<String, Bitmap>(MEMORY_CACHE_SIZE) {
        	       protected int sizeOf(String key, Bitmap value) {
        	           return value.getRowBytes() * value.getHeight();
        	       
        	   }};
        	
                final File diskCacheDir = getDiskCacheDir(context, DISK_CACHE_SUBDIR);
                _diskCache = DiskLruCache.open(diskCacheDir, APP_VERSION, VALUE_COUNT, DISK_CACHE_SIZE);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
 
    public void put(String key, Bitmap image) {
    	if (key == null || image == null) {
            return;
        }
    	
    	addBitmapToMemoryCache(key, image);
    	
        DiskLruCache.Editor editor = null;
        try {
            editor = _diskCache.edit(key);
            if (editor == null || image == null) {
                return;
            }
 
            if( writeBitmapToFile(image, editor)) {               
            	_diskCache.flush();
                editor.commit();
            } else {
                editor.abort();
            }   
        } catch (IOException e) {
            try {
                if ( editor != null ) {
                    editor.abort();
                }
            } catch (IOException ignored) {
            }           
        }
 
    }
 
    public Bitmap getBitmap(String key) {
    	if (key == null) {
            return null;
        }
    	
    	Bitmap bitmap = getBitmapFromMemCache(key);
        if(bitmap != null) {
        	return bitmap;
        }
        
        DiskLruCache.Snapshot snapshot = null;
        try { 
            snapshot = _diskCache.get(key);
            if ( snapshot == null ) {
                return null;
            }
            final InputStream in = snapshot.getInputStream(0);
            if ( in != null ) {
                final BufferedInputStream buffIn = 
                new BufferedInputStream( in, Utilities.IO_BUFFER_SIZE );
                bitmap = BitmapFactory.decodeStream( buffIn );              
            }   
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
            if ( snapshot != null ) {
                snapshot.close();
            }
        }
 
        return bitmap; 
    }
 
    public boolean containsKey( String key ) {
    	if (key == null) {
            return false;
        }
    	
        boolean contained = false;
        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = _diskCache.get( key );
            contained = snapshot != null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if ( snapshot != null ) {
                snapshot.close();
            }
        }
 
        return contained; 
    }
 
    public void clearCache() {
        try {
        	_diskCache.delete();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }
 
    public File getCacheFolder() {
        return _diskCache.getDirectory();
    }
 
    private static synchronized void addBitmapToMemoryCache(String key, Bitmap bitmap) {
    	if(key == null || bitmap == null) {
    		return;
    	}
    	
	    if (getBitmapFromMemCache(key) == null) {
	    	_memoryCache.put(key, bitmap);
	    }
	}

    private static Bitmap getBitmapFromMemCache(String key) {
    	if(key == null) {
    		return null;
    	}
    	
	    return _memoryCache.get(key);
	}	
	
    private boolean writeBitmapToFile(Bitmap bitmap, DiskLruCache.Editor editor)
        throws IOException, FileNotFoundException {
        OutputStream out = null;
        try {
            out = new BufferedOutputStream( editor.newOutputStream(0), Utilities.IO_BUFFER_SIZE );
            return bitmap.compress(CompressFormat.JPEG, 70, out);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
     
    private File getDiskCacheDir(Context context, String uniqueName) {
         return new File(context.getCacheDir().getPath() + File.separator + uniqueName);
    }             
}