package com.android.client.utilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;

public class Utilities {	
	
	public static final int IO_BUFFER_SIZE = 8 * 1024;
	    
    public static Bitmap getBitmap(String bitmapUrl) 
	{
		try 
		{
			URL url = new URL(bitmapUrl);
		    return BitmapFactory.decodeStream(url.openConnection().getInputStream());
		}
		catch(Exception ex) 
		{
			return null;
		}
	}
	
	public static File getExternalCacheDir(Context context) {
        if (hasExternalCacheDir()) {
            return context.getExternalCacheDir();
        }
 
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }
 
    public static boolean hasExternalCacheDir() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }
    
    public static URL EncodeUrl(String urlString) {
    	URL url = null;
		try {
			url = new URL(urlString);
			URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
			url = uri.toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}	
		
		return url;
    }
    
    public static String GetKey(String key) {
    	return key.toLowerCase().replaceAll("[^a-z0-9-]", "");
    }
}
