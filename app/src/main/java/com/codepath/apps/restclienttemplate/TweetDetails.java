package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class TweetDetails extends AppCompatActivity {

    private TwitterClient client;
    Tweet tweet;
    Context context;

    // the view objects
    @BindView(R.id.tvTweetText) TextView tvTweetText;
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvTime) TextView tvTime;
    @BindView(R.id.btReply) Button btReply;
    @BindView(R.id.btRetweet) Button btRetweet;
    @BindView(R.id.btLike) Button btLike;
    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @Nullable
    @BindView(R.id.ivPicture) ImageView ivPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);
        client = TwitterApp.getRestClient(getApplicationContext());

        // resolve the view objects
        ButterKnife.bind(this );

        // unwrap the movie passed in via intent, using its simple name as a key
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        // set the tweet
        tvTweetText.setText(tweet.getBody());
        tvName.setText(tweet.user.name);
        tvTime.setText(tweet.createdAt);

        Glide.with(ivProfileImage.getContext()).load(tweet.user.profileImageUrl).into(ivProfileImage);

        btLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                client.favTweet(tweet.uid, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if(tweet.favorited) {
                            v.setSelected(false);
                            tweet.favorited = false;
                        } else {
                            v.setSelected(true);
                            tweet.favorited = true;
                        }
                        Toast.makeText(getBaseContext(), "Liked!", Toast.LENGTH_LONG).show();
                    }
                    public void onFailure(Throwable e) {
                        Log.d("DEBUG", "Fetch timeline error: " + e.toString());
                    }

                    @Override
                    public void onFinish(){

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
                        Toast.makeText(getBaseContext(), "Retweeted!", Toast.LENGTH_LONG).show();
                    }
                    public void onFailure(Throwable e) {
                        Log.d("DEBUG", "Fetch timeline error: " + e.toString());
                    }
                });
            }
        });
    }

}
