package com.stirtrek.activity;

import stirtrek.activity.R;

import com.stirtrek.adapter.ViewPagerAdapter;
import com.stirtrek.common.IResponseListener;
import com.stirtrek.application.StirTrek.App;
import com.stirtrek.model.Response;
import com.android.common.TitlePageIndicator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.widget.Toast;

public class ScheduleActivity extends BaseActivity {    
	
	public ScheduleActivity() {
		super(R.layout.schedule_pager);
		
		App.SetOnResponseReceived(new IResponseListener() {
			
			public void OnResponseReceived(Response response) {
				ResponseReceived(response);				
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		Refresh();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private void Refresh() {
		SetBusy(true);			
		
		Response response = App.GetResponse();
		if(response != null)
			ResponseReceived(response);		
	}
	
	protected void ResponseReceived(final Response response) {
		if(response == null)
		{
			Toast toast=Toast.makeText(getBaseContext(), "Could not retrieve schedule.", Toast.LENGTH_LONG);
		    toast.setGravity(Gravity.CENTER, 0, 0);
		    toast.show();

			return;
		}		
		
		Context context = getBaseContext();
		
		ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(response, context);
		
		ViewPager viewPager = (ViewPager)findViewById(R.id.viewpager);		
		viewPager.setAdapter(pagerAdapter);		
		
		viewPager.setFocusable(true);
		viewPager.setClickable(true);
		
		//Bind the title indicator to the adapter
		TitlePageIndicator titleIndicator = (TitlePageIndicator)findViewById(R.id.titles);
		titleIndicator.setViewPager(viewPager);			
		
		viewPager.setCurrentItem(0, true);
		
		SetBusy(false);		
	}
}