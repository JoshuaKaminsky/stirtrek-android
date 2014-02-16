package com.android.client.utilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Utilities {	
	
	public static final int IO_BUFFER_SIZE = 8 * 1024;
	    
	public Bitmap getBitmap(String bitmapUrl) 
	{
		try 
		{
			URL url = new URL(bitmapUrl);
		    return BitmapFactory.decodeStream(url.openConnection().getInputStream());
		}
		catch(Exception ex) 
		{
			Log.e("com.android.client.utilities",ex.getMessage());
			return null;
		}
	}
	
	public static void getBitmapAsync(String bitmapUrl, ImageView imageView) {
		if (!cancelPotentialDownload(bitmapUrl, imageView)) {
			return;	         
	     }
		
		HttpGetImageAsyncTask task = new HttpGetImageAsyncTask(imageView);
        DownloadingDrawable downloadedDrawable = new DownloadingDrawable(task);
        imageView.setImageDrawable(downloadedDrawable);
        task.execute(bitmapUrl);
	}			
	
	public void scaleImage(ImageView view, int max)
	{
	    // Get the ImageView and its bitmap
	    Drawable drawing = view.getDrawable();
	    Bitmap bitmap = ((BitmapDrawable)drawing).getBitmap();

	    // Get current dimensions AND the desired bounding box
	    int width = bitmap.getWidth();
	    int height = bitmap.getHeight();
	    int bounding = dpToPx(max, view.getContext());
	    
	    // Determine how much to scale: the dimension requiring less scaling is
	    // closer to the its side. This way the image always stays inside your
	    // bounding box AND either x/y axis touches it.  
	    float xScale = ((float) bounding) / width;
	    float yScale = ((float) bounding) / height;
	    float scale = (xScale <= yScale) ? xScale : yScale;
	    
	    // Create a matrix for the scaling and add the scaling data
	    Matrix matrix = new Matrix();
	    matrix.postScale(scale, scale);

	    // Create a new bitmap and convert it to a format understood by the ImageView 
	    Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
	    width = scaledBitmap.getWidth(); // re-use
	    height = scaledBitmap.getHeight(); // re-use
	    
	    // Apply the scaled bitmap
	    view.setImageDrawable(new BitmapDrawable(scaledBitmap));

	    // Now change ImageView's dimensions to match the scaled image
	    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams(); 
	    params.width = width;
	    params.height = height;
	    view.setLayoutParams(params);
	}

	private int dpToPx(int dp, Context context)	{
	    float density = context.getResources().getDisplayMetrics().density;
	    return Math.round((float)dp * density);
	}
	
	private static boolean cancelPotentialDownload(String url, ImageView imageView) {
		HttpGetImageAsyncTask imageTask = getImageDownloaderTask(imageView);

	    if (imageTask != null) {
	        String bitmapUrl = imageTask.getUrl();
	        if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
	        	imageTask.cancel(true);
	        } else {
	            return false;
	        }
	    }
	    return true;
	}
	
	private static HttpGetImageAsyncTask getImageDownloaderTask(ImageView imageView) {
	    if (imageView != null) {
	        Drawable drawable = imageView.getDrawable();
	        if (drawable instanceof DownloadingDrawable) {
	        	DownloadingDrawable downloadingDrawable = (DownloadingDrawable)drawable;
	            return downloadingDrawable.getImageDownloaderTask();
	        }
	    }
	    
	    return null;
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
