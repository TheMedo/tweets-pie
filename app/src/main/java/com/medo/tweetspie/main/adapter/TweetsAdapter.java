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

import com.medo.tweetspie.R;
import com.medo.tweetspie.database.model.RealmTweet;
import com.medo.tweetspie.utils.FormatUtils;
import com.medo.tweetspie.utils.ImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
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

    private RealmTweet tweet;

    TweetViewHolder(View view) {

      super(view);
      ButterKnife.bind(this, view);
    }

    void showTweet(RealmTweet tweet) {

      this.tweet = tweet;
      // bind the tweet data to the views
      if (tweet.getUser() != null) {
        ImageUtils.loadUserAvatar(context, imageAvatar, tweet.getUser());
        textName.setText(tweet.getUser().getName());
        textUsername.setText("@");
        textUsername.append(tweet.getUser().getScreenName());
      }
      textDate.setText(FormatUtils.toRelativeDate(tweet.getCreatedAt()));
      textDate.setPaintFlags(textDate.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
      textText.setText(tweet.getText());
    }

    @OnClick(R.id.image_avatar)
    public void onAvatarClick() {
      // TODO add username
      presenter.onAvatarClick();
    }

    @OnClick(R.id.text_date)
    public void onDateClick() {
      // TODO add tweet id
      presenter.onDateClick();
    }

    @OnClick(R.id.text_text)
    public void onMediaClick() {
      // TODO add media url
      presenter.onMediaClick();
    }
  }
}
