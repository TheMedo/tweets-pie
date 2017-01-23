package com.medo.tweetspie.main.adapter;


import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.medo.tweetspie.R;
import com.medo.tweetspie.database.model.RealmTweet;
import com.medo.tweetspie.database.model.RealmTweetEntity;
import com.medo.tweetspie.database.model.RealmTweetUser;
import com.medo.tweetspie.utils.Constant;
import com.medo.tweetspie.utils.FormatUtils;
import com.medo.tweetspie.utils.ImageUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;


public class TweetsAdapter extends RealmRecyclerViewAdapter<RealmTweet, TweetsAdapter.TweetViewHolder> {

  private final AdapterContract.Presenter presenter;

  public TweetsAdapter(@NonNull Context context,
                       @Nullable OrderedRealmCollection<RealmTweet> data,
                       @NonNull AdapterContract.Presenter presenter) {

    super(context, data, true);
    this.presenter = presenter;
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
    @BindView(R.id.text_retweeted_by)
    TextView textRetweetedBy;
    @BindView(R.id.text_text)
    TextView textText;
    @BindView(R.id.image_media)
    ImageView imageMedia;
    @BindView(R.id.image_transparency)
    ImageView imageTransparency;
    @BindView(R.id.text_media_type)
    TextView textMediaType;
    @BindView(R.id.text_retweets)
    CheckedTextView toggleRetweets;
    @BindView(R.id.text_favorites)
    CheckedTextView toggleFavorites;
    @BindView(R.id.text_score)
    TextView textScore;

    private RealmTweet tweet;

    TweetViewHolder(View view) {

      super(view);
      ButterKnife.bind(this, view);
    }

    // TODO adapters need special handling in the EBI architecture
    void showTweet(RealmTweet tweet) {
      // bind the tweet data to the views
      this.tweet = tweet;
      // set the user data
      final RealmTweetUser user = tweet.getUser();
      ImageUtils.loadUserAvatar(context, imageAvatar, user);
      textName.setText(user.getName());
      textUsername.setText("@");
      textUsername.append(user.getScreenName());

      // set the date
      textDate.setText(FormatUtils.toRelativeDate(tweet.getCreatedAt()));
      textDate.setPaintFlags(textDate.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

      // set the retweeted by if applicable
      RealmTweetUser retweetedBy = tweet.getRetweetedBy();
      if (retweetedBy != null) {
        textRetweetedBy.setVisibility(View.VISIBLE);
        textRetweetedBy.setText(String.format("Retweeted by @%s", retweetedBy.getScreenName()));
      }
      else {
        textRetweetedBy.setVisibility(View.GONE);
      }

      // set the text and add links keeping the new lines
      textText.setText(Html.fromHtml(tweet.getText().replace("\n", "<br>")));
      FormatUtils.addLinks(textText);

      // set the media
      imageTransparency.setVisibility(View.GONE);
      imageMedia.setVisibility(View.GONE);
      textMediaType.setVisibility(View.GONE);

      List<RealmTweetEntity> extendedEntities = tweet.getExtendedEntities();
      if (extendedEntities != null && !extendedEntities.isEmpty()) {
        RealmTweetEntity entity = extendedEntities.get(0);
        // TODO handle media types

        // load the media image
        imageMedia.setVisibility(View.VISIBLE);
        ImageUtils.loadEntityMedia(context, imageMedia, entity);

        if (Constant.MediaType.ANIMATED_GIF.equalsIgnoreCase(entity.getType())) {
          // show the gif type
          imageTransparency.setVisibility(View.VISIBLE);
          textMediaType.setVisibility(View.VISIBLE);
          textMediaType.setText(Constant.GIF);
        }
        else if (Constant.MediaType.VIDEO.equalsIgnoreCase(entity.getType())) {
          // show the video type
          imageTransparency.setVisibility(View.VISIBLE);
          textMediaType.setVisibility(View.VISIBLE);
          textMediaType.setText(Constant.VID);
        }
        else if (extendedEntities.size() > 1) {
          // show count in case of multiple images
          imageTransparency.setVisibility(View.VISIBLE);
          textMediaType.setVisibility(View.VISIBLE);
          textMediaType.setText("+");
          textMediaType.append(String.valueOf(extendedEntities.size() - 1));
        }
      }

      // set locked user
      final boolean locked = user.isLocked();
      textName.setCompoundDrawablesWithIntrinsicBounds(locked ? R.drawable.ic_lock : 0, 0, 0, 0);

      // set the retweets and disable retweeting if user is locked
      toggleRetweets.setChecked(tweet.isRetweeted());
      toggleRetweets.setText(FormatUtils.formatNumber(tweet.getRetweetCount()));
      toggleRetweets.setEnabled(!locked);

      // set the favorites
      toggleFavorites.setChecked(tweet.isFavorited());
      toggleFavorites.setText(FormatUtils.formatNumber(tweet.getFavoriteCount()));

      // set the score
      textScore.setText(FormatUtils.formatNumber(tweet.getScore()));
    }

    @OnClick({R.id.image_avatar, R.id.text_username})
    void onAvatarClick() {

      presenter.onAvatarClick(tweet.getUser().getScreenName());
    }

    @OnClick(R.id.text_date)
    void onDateClick() {

      presenter.onDateClick(tweet.getIdStr(), tweet.getUser().getScreenName());
    }

    @OnClick(R.id.image_media)
    void onMediaClick() {

      presenter.onMediaClick(tweet.getIdStr());
    }

    @OnClick(R.id.text_retweets)
    void onRetweetClicked() {

      presenter.onRetweetClick(tweet.getIdStr());
    }

    @OnClick(R.id.text_favorites)
    void onFavoriteClicked() {

      presenter.onFavoriteClick(tweet.getIdStr());
    }
  }
}
