package com.stirtrek.activity;

import stirtrek.activity.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.android.common.TitlePageIndicator;
import com.stirtrek.adapter.ViewPagerAdapter;
import com.stirtrek.application.StirTrek.App;
import com.stirtrek.contract.IResponseListener;
import com.stirtrek.model.Response;

public class ScheduleActivity extends BaseActivity {    
	
	public ScheduleActivity() {
		super(R.layout.schedule_pager);
		
		App.SetResponseListener(new IResponseListener() {
			
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
		
		setBusy(true, this);
		
		App.RefreshResponse();
	}
	
	private void Refresh() {	
		setBusy(true, this);
		
		Response response = App.GetResponse();
		if(response == null)
		{
			Toast toast=Toast.makeText(getBaseContext(), "Could not retrieve schedule.", Toast.LENGTH_LONG);
		    toast.setGravity(Gravity.CENTER, 0, 0);
		    toast.show();
			
			setBusy(false, this);			

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
		
		setBusy(false, this);			
	}
	
	protected void ResponseReceived(final Response response) {
		Refresh();	
	}
	
	private void setBusy(Boolean busy, ScheduleActivity context) {
		int visible = busy ? View.VISIBLE : View.INVISIBLE;
		
		context.findViewById(R.id.schedule_busy_bar).setVisibility(visible);
	}
}