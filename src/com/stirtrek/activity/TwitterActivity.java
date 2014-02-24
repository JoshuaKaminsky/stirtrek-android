package com.stirtrek.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import stirtrek.activity.R;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.client.utilities.HttpAsyncTask;
import com.android.contract.IResultCallback;
import com.stirtrek.adapter.TwitterAdapter;
import com.twitter.model.QueryResult;
import com.twitter.model.TokenResponse;

public class TwitterActivity extends BaseActivity implements IResultCallback<QueryResult>{
	
	private static String twitterTokenhUrl = "https://api.twitter.com/oauth2/token";
	private static String twitterSearchUrl = "https://api.twitter.com/1.1/search/tweets.json?q=stirtrek-filter:retweets&rpp=50";
	
	private static String apiKey = "oLTvXx9TlgWRe8Omo23o5w";
	private static String apiSecret = "QgrSMsEdTTFAxDmGV0t1SUk0utEeDkFlEASutpWqQ";
	
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
		
		HttpPost postRequest = new HttpPost(twitterTokenhUrl);
		
		try {	
			
			//get bearer token			
			postRequest.setHeader("Authorization", getBasicAuth());
			postRequest.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		
			postRequest.setEntity(new StringEntity("grant_type=client_credentials"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final TwitterActivity context = this;
		
		new HttpAsyncTask<String, TokenResponse>(new IResultCallback<TokenResponse>() {

			@Override
			public Class<TokenResponse> GetType() {
				return TokenResponse.class;
			}

			@Override
			public void Callback(TokenResponse result) {
				if(result == null || !result.tokenType.equalsIgnoreCase("bearer")) {
					Toast toast=Toast.makeText(context, "Could not retrieve tweets.", Toast.LENGTH_LONG);
				    toast.setGravity(Gravity.CENTER, 0, 0);
				    toast.show();
				    
				    SetBusy(false);
				    
				    return;
				}
				
				new HttpAsyncTask<Void, QueryResult>("Bearer " + result.accessToken, context).Get(twitterSearchUrl);
			}
		}).Execute(postRequest);
		
	}
	
	public void Callback(QueryResult result) {
		SetBusy(false);
		if(result == null)
		{
			Toast toast=Toast.makeText(this, "Could not retrieve tweets.", Toast.LENGTH_LONG);
		    toast.setGravity(Gravity.CENTER, 0, 0);
		    toast.show();
		    
		    return;
		}
		
		ListView listView = (ListView)findViewById(R.id.twitter_listview);
		listView.setAdapter(new TwitterAdapter(getBaseContext(), result.statuses));	
		
		ImageButton imageButton = (ImageButton)findViewById(R.id.twitter_refresh);
		imageButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Refresh();
			}
		});
	}

	public Class<QueryResult> GetType() {
		return QueryResult.class;
	}	
	
	private String getBasicAuth() throws UnsupportedEncodingException {
		return "Basic " + Base64.encodeToString((URLEncoder.encode(apiKey, "UTF-8") + ':' +URLEncoder.encode(apiSecret, "UTF-8") ).getBytes(), Base64.NO_WRAP);
	}
}
