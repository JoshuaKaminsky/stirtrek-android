package com.stirtrek.activity;

import com.stirtrek.application.StirTrek.App;
import com.stirtrek.contract.IResponseListener;
import com.stirtrek.model.Response;

import stirtrek.activity.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
 
public class SplashScreen extends Activity {
 
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2500;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
 
        App.initialize(getApplicationContext());
        
        App.RefreshCache(this);
		App.RefreshInterests(getContentResolver());
	    App.RefreshResponse();		
		
	    if(App.GetResponse() == null) {
	    	findViewById(R.id.splash_busy).setVisibility(View.VISIBLE);
	    	
	    	App.SetResponseListener(new IResponseListener() {
				
				@Override
				public void OnResponseReceived(Response response) {
					App.RemoveResponseListener(this);
					
					goToMainIntent();
				}
			});
	    } else {
	    	new Handler().postDelayed(new Runnable() {
	    		 
	            @Override
	            public void run() {
                	goToMainIntent();  
	            }
	        }, SPLASH_TIME_OUT);
	    }            
    }
 
    private void goToMainIntent() {
    	Intent i = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(i);
        
        finish();
    }
}