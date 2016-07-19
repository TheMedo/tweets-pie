package com.medo.tweetspie.main.adapter;

import android.support.annotation.NonNull;

import com.medo.tweetspie.utils.FormatUtils;


public class AdapterPresenter implements AdapterContract.Actions {

  private final AdapterContract.View view;

  public AdapterPresenter(AdapterContract.View view) {

    this.view = view;
  }

  @Override
  public void onDateClick(@NonNull String id, @NonNull String screenName) {

    view.openTweet(FormatUtils.getTweetUrl(id, screenName));
  }

  @Override
  public void onAvatarClick() {

    view.openUser();
  }

  @Override
  public void onMediaClick() {

    view.openMedia();
  }

  @Override
  public void onRetweetClick(@NonNull String id) {

    view.toggleRetweet(id);
  }

  @Override
  public void onFavoriteClick(@NonNull String id) {

    view.toggleFavorite(id);
  }
}
