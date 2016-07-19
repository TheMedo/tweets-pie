package com.medo.tweetspie.main.adapter;


import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.medo.tweetspie.R;
import com.medo.tweetspie.database.model.RealmTweet;
import com.medo.tweetspie.utils.FormatUtils;
import com.medo.tweetspie.utils.ImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;


public class TweetsAdapter extends RealmRecyclerViewAdapter<RealmTweet, TweetsAdapter.TweetViewHolder> {

  private final AdapterContract.Actions presenter;

  public TweetsAdapter(@NonNull Context context,
                       @Nullable OrderedRealmCollection<RealmTweet> data,
                       @NonNull AdapterContract.View view) {

    super(context, data, true);
    presenter = new AdapterPresenter(view);
  }

  @Override
  public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    View itemView = inflater.inflate(R.layout.layout_tweet, parent, false);
    return new TweetViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(TweetViewHolder holder, int position) {
    // show the tweet
    RealmTweet tweet = getData().get(position);
    holder.showTweet(tweet);
  }

  class TweetViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image_avatar)
    ImageView imageAvatar;
    @BindView(R.id.text_name)
    TextView textName;
    @BindView(R.id.text_username)
    TextView textUsername;
    @BindView(R.id.text_date)
    TextView textDate;
    @BindView(R.id.text_text)
    TextView textText;
    @BindView(R.id.toggle_retweets)
    ToggleButton toggleRetweets;
    @BindView(R.id.toggle_favorites)
    ToggleButton toggleFavorites;
    @BindView(R.id.text_score)
    TextView textScore;

    private RealmTweet tweet;

    TweetViewHolder(View view) {

      super(view);
      ButterKnife.bind(this, view);
    }

    void showTweet(RealmTweet tweet) {

      this.tweet = tweet;
      // bind the tweet data to the views
      if (tweet.getUser() != null) {
        // set the user data
        ImageUtils.loadUserAvatar(context, imageAvatar, tweet.getUser());
        textName.setText(tweet.getUser().getName());
        textUsername.setText("@");
        textUsername.append(tweet.getUser().getScreenName());
      }

      // set the date
      textDate.setText(FormatUtils.toRelativeDate(tweet.getCreatedAt()));
      textDate.setPaintFlags(textDate.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

      // set the text and add links
      textText.setText(tweet.getText());
      FormatUtils.addLinks(textText);

      // set the retweets
      toggleRetweets.setChecked(tweet.isRetweeted());
      toggleRetweets.setText(String.valueOf(tweet.getRetweetCount()));

      // set the favorites
      toggleFavorites.setChecked(tweet.isFavorited());
      toggleFavorites.setText(String.valueOf(tweet.getFavoriteCount()));

      // set the score
      textScore.setText(String.valueOf(tweet.getScore()));
    }

    @OnClick(R.id.image_avatar)
    void onAvatarClick() {
      // TODO add username
      presenter.onAvatarClick();
    }

    @OnClick(R.id.text_date)
    void onDateClick() {
      // TODO add tweet id
      presenter.onDateClick();
    }

    @OnClick(R.id.text_text)
    void onMediaClick() {
      // TODO add media url
      presenter.onMediaClick();
    }

    @OnCheckedChanged(R.id.toggle_retweets)
    void onRetweetClicked(boolean checked) {
      // TODO toggle retweet
    }

    @OnCheckedChanged(R.id.toggle_favorites)
    void onFavoriteClicked(boolean checked) {
      // TODO toggle favorite
    }
  }
}
