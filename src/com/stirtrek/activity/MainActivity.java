package com.stirtrek.activity;

import stirtrek.activity.R;
import com.stirtrek.application.StirTrek.App;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;

public class MainActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		App.RefreshInterests(getContentResolver());
	    App.RefreshResponse();
		
		setContentView(R.layout.main);
		
		Resources res = getResources(); 
	    TabHost tabHost = getTabHost(); 

	    //have to set the divider
	    tabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);
	    
	    //calendar tab
	    Intent intent = new Intent().setClass(this, ScheduleActivity.class);
	    TabHost.TabSpec spec = tabHost.newTabSpec("schedule")
	    							  .setIndicator(CreateTab(res.getDrawable(R.drawable.calendar)))
	    							  .setContent(intent);
	    tabHost.addTab(spec);

	    //twitter tab
	    intent = new Intent().setClass(this, TwitterActivity.class);
	    spec = tabHost.newTabSpec("twitter")
	    			  .setIndicator(CreateTab(res.getDrawable(R.drawable.twitter)))
	    			  .setContent(intent);
	    tabHost.addTab(spec);

	  	//speakers tab
	    intent = new Intent().setClass(this, SpeakerActivity.class);
	    spec = tabHost.newTabSpec("speakers")
	    			  .setIndicator(CreateTab(res.getDrawable(R.drawable.speaker)))
	    			  .setContent(intent);
	    tabHost.addTab(spec);
	    
	    //interests tab
	    intent = new Intent().setClass(this, InterestsActivity.class);
	    spec = tabHost.newTabSpec("interests")
	    			  .setIndicator(CreateTab(res.getDrawable(R.drawable.star_icon)))
	    			  .setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTabByTag("schedule");	    
	}
	
	private View CreateTab(Drawable image)
	{
		View view = LayoutInflater.from(getBaseContext()).inflate(R.drawable.tab_background, null);
		
		ImageView imageView = (ImageView)view.findViewById(R.id.tabImage);
		imageView.setImageDrawable(image);
		
		return view;
	}
}
