package com.medo.tweetspie.main.adapter;

public class AdapterPresenter implements AdapterContract.Actions {

  private final AdapterContract.View view;

  public AdapterPresenter(AdapterContract.View view) {

    this.view = view;
  }

  @Override
  public void onDateClick() {

    view.openTweet();
  }

  @Override
  public void onAvatarClick() {

    view.openUser();
  }

  @Override
  public void onMediaClick() {

    view.openMedia();
  }
}
