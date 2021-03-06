package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {
    private TwitterClient client;
    TextView etCompose;
    Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        etCompose = (EditText) findViewById(R.id.etCompose);
        tweet = Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        if(tweet != null) {
            Log.i("screenName", tweet.user.screenName);
            if(!tweet.user.screenName.equals("")){
                Log.i("Compose Activity", "gets here");
                etCompose.setText("@" + tweet.user.screenName);
            }
        }
        client = TwitterApp.getRestClient(getApplicationContext());
    }

    public void onSubmit(View v) {

        client.sendTweet(etCompose.getText().toString(), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Log.i("ComposeActivity", "success1");
                    tweet = Tweet.fromJSON(response);
                    Intent data = new Intent(getBaseContext(), TimelineActivity.class);
                    data.putExtra("tweet", Parcels.wrap(tweet));
                    data.putExtra("code", 3);
                    setResult(RESULT_OK, data);
                    Log.i("ComposeActivity", "success");
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
