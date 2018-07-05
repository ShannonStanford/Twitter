package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class TweetDetails extends AppCompatActivity {

    private TwitterClient client;
    Tweet tweet;

    // the view objects
    TextView tvTweetText;
    Button btReply;
    Button btRetweet;
    Button btLike;

    //ImageView im;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);
        client = TwitterApp.getRestClient(getApplicationContext());

        // resolve the view objects
        tvTweetText = (TextView) findViewById(R.id.tvTweetText);
        btLike = (Button) findViewById(R.id.btLike);
        btRetweet = (Button) findViewById(R.id.btRetweet);

        // unwrap the movie passed in via intent, using its simple name as a key
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        // set the tweet
        tvTweetText.setText(tweet.getBody());


        btLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                client.favTweet(tweet.uid, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            tweet = Tweet.fromJSON(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    public void onFailure(Throwable e) {
                        Log.d("DEBUG", "Fetch timeline error: " + e.toString());
                    }
                });

            }
        });


        btRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                client.retweet(tweet.uid, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            tweet = Tweet.fromJSON(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    public void onFailure(Throwable e) {
                        Log.d("DEBUG", "Fetch timeline error: " + e.toString());
                    }
                });

            }
        });
    }


//   client.sendTweet(etCompose.getText().toString(), new JsonHttpResponseHandler(){
//        @Override
//        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//            try {
//                Log.i("ComposeActivity", "success1");
//                tweet = Tweet.fromJSON(response);
//                Intent data = new Intent(getBaseContext(), TimelineActivity.class);
//                data.putExtra("tweet", Parcels.wrap(tweet));
//                data.putExtra("code", 3);
//                setResult(RESULT_OK, data);
//                Log.i("ComposeActivity", "success");
//                finish();
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//            super.onFailure(statusCode, headers, responseString, throwable);
//        }
//    });




}
