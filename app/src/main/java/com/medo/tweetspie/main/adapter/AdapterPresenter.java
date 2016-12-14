package com.medo.tweetspie.main.adapter;

import android.support.annotation.NonNull;

import com.medo.tweetspie.base.AbsViewPresenter;
import com.medo.tweetspie.database.RealmTransaction;
import com.medo.tweetspie.rest.TwitterTransaction;
import com.medo.tweetspie.utils.FormatUtils;


public class AdapterPresenter extends AbsViewPresenter<AdapterContract.View>
        implements AdapterContract.Presenter {

  private final TwitterTransaction twitterInteractor;
  private final RealmTransaction realmInteractor;

  public AdapterPresenter(TwitterTransaction twitterInteractor,
                          RealmTransaction realmInteractor) {

    this.twitterInteractor = twitterInteractor;
    this.realmInteractor = realmInteractor;
  }

  @Override
  public void onDateClick(@NonNull String id, @NonNull String screenName) {

    getView().openUrl(FormatUtils.getTweetUrl(id, screenName));
  }

  @Override
  public void onAvatarClick(@NonNull String screenName) {

    getView().openUrl(FormatUtils.getUserUrl(screenName));
  }

  @Override
  public void onMediaClick(@NonNull String id) {

    getView().openTweetMedia(id);
  }

  @Override
  public void onRetweetClick(@NonNull String id) {

    boolean retweet = realmInteractor.toggleRetweet(id);
    // TODO implement callback
    twitterInteractor.retweetStatus(id, retweet, null);
  }

  @Override
  public void onFavoriteClick(@NonNull String id) {

    boolean favorite = realmInteractor.toggleFavorite(id);
    // TODO implement callback
    twitterInteractor.favoriteStatus(id, favorite, null);
  }
}
