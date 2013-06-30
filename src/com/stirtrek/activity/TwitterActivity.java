package com.stirtrek.activity;

import stirtrek.activity.R;

import com.stirtrek.adapter.TwitterAdapter;
import com.twitter.model.SearchResponse;

import com.android.client.utilities.HttpGetAsyncTask;
import com.android.client.utilities.IResultCallback;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class TwitterActivity extends BaseActivity implements IResultCallback<SearchResponse>{
	
	private static String twitterSearchUrl = "http://search.twitter.com/search.json?q=stirtrek-filter:retweets&rpp=50";
	
	public TwitterActivity() {
		super(R.layout.twitter);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Refresh();
	}		

	public void Refresh() {
		SetBusy(true);
		new HttpGetAsyncTask<SearchResponse>(this).execute(twitterSearchUrl);
	}
	
	public void Callback(SearchResponse result) {
		SetBusy(false);
		if(result == null)
		{
			Toast toast=Toast.makeText(getBaseContext(), "Could not retrieve tweets.", Toast.LENGTH_LONG);
		    toast.setGravity(Gravity.CENTER, 0, 0);
		    toast.show();
		    
		    return;
		}
		
		ListView listView = (ListView)findViewById(R.id.twitter_listview);
		listView.setAdapter(new TwitterAdapter(getBaseContext(), result.results));	
		
		ImageButton imageButton = (ImageButton)findViewById(R.id.twitter_refresh);
		imageButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Refresh();
			}
		});
	}

	public Class<SearchResponse> GetType() {
		return SearchResponse.class;
	}			
}
