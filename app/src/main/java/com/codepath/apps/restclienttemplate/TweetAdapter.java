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

import org.parceler.Parcels;

import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{

    private List<Tweet> mTweets;
    Context context;

    //pass in the Tweets array in the constructor
    public TweetAdapter(List<Tweet> tweets){
        mTweets = tweets;
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

        Glide.with(context).load(tweet.user.profileImageUrl).into(holder.ivProfileImage);

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

    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }


    //create ViewHolder class

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public Button btReply;

        public ViewHolder(View itemView){
            super(itemView);

            //perform findViewByID lookups

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            btReply = (Button) itemView.findViewById(R.id.btReply);
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
