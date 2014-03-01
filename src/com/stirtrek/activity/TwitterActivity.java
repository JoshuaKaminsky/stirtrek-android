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
import android.widget.ListView;
import android.widget.Toast;

import com.android.client.utilities.HttpAsyncTask;
import com.android.common.PullToRefreshListView;
import com.android.common.PullToRefreshListView.OnRefreshListener;
import com.android.contract.IResultCallback;
import com.stirtrek.adapter.TwitterAdapter;
import com.twitter.model.QueryResult;
import com.twitter.model.TokenResponse;

public class TwitterActivity extends BaseActivity {
	
	private static String twitterTokenhUrl = "https://api.twitter.com/oauth2/token";
	private static String twitterSearchUrl = "https://api.twitter.com/1.1/search/tweets.json?q=stirtrek-filter:retweets&rpp=50";
	
	private static String apiKey = "oLTvXx9TlgWRe8Omo23o5w";
	private static String apiSecret = "QgrSMsEdTTFAxDmGV0t1SUk0utEeDkFlEASutpWqQ";
	
	private String _token;
	
	private Boolean _refreshing = false;
	
	public TwitterActivity() {
		super(R.layout.twitter);		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		
		final TwitterActivity context = this;
		
		((PullToRefreshListView) findViewById(R.id.twitter_listview)).setOnRefreshListener(new OnRefreshListener() {
		    @Override
		    public void onRefresh() {
		        if(!_refreshing) {
		        	context.Refresh();
		        }
		    }
		});
		
		Refresh();
	}		
	
	@Override
	protected void onResume() {
		super.onResume();
	};

	public void Refresh() {
		String token = getBearerToken();
		if(token != null && token != "") {
			getTweets(token, this);
			return;
		}		
		
		setBusy(true, this);
		
		final TwitterActivity context = this;
		
		IResultCallback<TokenResponse> callback = new IResultCallback<TokenResponse>() {

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
				    
				    context.setBusy(false, context);
				    
				    return;
				}
				
				context.setBearerToken(result.accessToken);
				
				context.getTweets(result.accessToken, context);
			}
		};
		
		authorize(callback);
	}
	
	public void authorize(IResultCallback<TokenResponse> callback) {
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
		
		new HttpAsyncTask<String, TokenResponse>(callback).Execute(postRequest);
	}
	
	public void getTweets(String accessToken, final TwitterActivity context) {	
		context.setBusy(true, context);
		
		IResultCallback<QueryResult> callback = new IResultCallback<QueryResult>() {

			@Override
			public Class<QueryResult> GetType() {
				return QueryResult.class;
			}

			@Override
			public void Callback(QueryResult result) {
				if(result == null)
				{
					Toast toast=Toast.makeText(context, "Could not retrieve tweets.", Toast.LENGTH_LONG);
				    toast.setGravity(Gravity.CENTER, 0, 0);
				    toast.show();
				    
				    context.setBusy(false, context);
					
				    return;
				}
				
				ListView listView = (ListView)findViewById(R.id.twitter_listview);
				listView.setAdapter(new TwitterAdapter(getBaseContext(), result.statuses));	
				
				context.setBusy(false, context);	
			}
		};
		
		new HttpAsyncTask<Void, QueryResult>("Bearer " + accessToken, callback).Get(twitterSearchUrl);
	}
	
	private void setBearerToken(String token) {
		_token = token;
	}
	
	private String getBearerToken() {
		return _token;
	}
	
	private String getBasicAuth() throws UnsupportedEncodingException {
		return "Basic " + Base64.encodeToString((URLEncoder.encode(apiKey, "UTF-8") + ':' +URLEncoder.encode(apiSecret, "UTF-8") ).getBytes(), Base64.NO_WRAP);
	}

	private void setBusy(Boolean busy, TwitterActivity context) {
		_refreshing = busy;
		
		int visible = busy ? View.VISIBLE : View.INVISIBLE;
		
		context.findViewById(R.id.twitter_busy_bar).setVisibility(visible);
		
		if(!busy){
			((PullToRefreshListView) context.findViewById(R.id.twitter_listview)).onRefreshComplete();
		}
	}
}
