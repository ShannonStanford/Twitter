package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{

    private List<Tweet> mTweets;
    Context context;
    TwitterClient client;



    //pass in the Tweets array in the constructor
    public TweetAdapter(List<Tweet> tweets, TwitterClient client){

        mTweets = tweets;
        this.client = client;

    }

    //for each row inflate the layout and cache references into ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);

        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    //bind the values based on the position of the element

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get the data according to position
        final Tweet tweet = mTweets.get(position);

        //populate the views according to this data
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvTime.setText(tweet.createdAt);
        holder.tvScreenName.setText("@" + tweet.user.screenName);


        Glide.with(context).load(tweet.user.profileImageUrl).into(holder.ivProfileImage);

        if(tweet.mediaUrl != null) {
            Glide.with(context).load(tweet.user.profileImageUrl).into(holder.ivPicture);
        } else {
            holder.ivPicture.setVisibility(View.GONE);
        }

        holder.btReply.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent intent = new Intent(context, ComposeActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                intent.putExtra("screen", Parcels.wrap(tweet.user.screenName));
                Log.i("screenName", tweet.user.screenName);
                intent.putExtra("tweet_id", Parcels.wrap(tweet.uid));
                // show the activity
                context.startActivity(intent);
            }
        });

        holder.btLike.setOnClickListener(new View.OnClickListener() {
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

                        Log.d("DEBUG", "worked");
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

        holder.btRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                client.retweet(tweet.uid, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        v.setSelected(true);

                        Log.d("DEBUG", "worked");
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




    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    //create ViewHolder class

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
        @BindView(R.id.ivPicture) ImageView ivPicture;
        @BindView(R.id.tvUserName) TextView tvUsername;
        @BindView(R.id.tvBody) TextView tvBody;
        @BindView(R.id.btReply) Button btReply;
        @BindView(R.id.btRetweet) Button btRetweet;
        @BindView(R.id.btLike) Button btLike;
        @BindView(R.id.tvTime) TextView tvTime;
        @BindView(R.id.tvScreenName) TextView tvScreenName;



        public ViewHolder(View itemView){
            super(itemView);

            //perform findViewByID lookups
//            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
////            ivPicture = (ImageView) itemView.findViewById(R.id.ivPicture);
////            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
////            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
////            btReply = (Button) itemView.findViewById(R.id.btReply);
////            btRetweet = (Button) itemView.findViewById(R.id.btRetweet);
////            btLike = (Button) itemView.findViewById(R.id.btLike);
////            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
////            tvScreenName = (TextView) itemView.findViewById(R.id.tvScreenName);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Log.i("TweetAdapter", "onClick clicked");
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Tweet tweet = mTweets.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, TweetDetails.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                // show the activity
                context.startActivity(intent);
            }
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }
}
