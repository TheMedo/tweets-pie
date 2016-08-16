package com.medo.tweetspie.main.adapter;

import android.support.annotation.NonNull;

import com.medo.tweetspie.base.AbsViewPresenter;
import com.medo.tweetspie.utils.FormatUtils;


public class AdapterPresenter extends AbsViewPresenter<AdapterContract.View>
        implements AdapterContract.Presenter {


  public AdapterPresenter() {

  }

  @Override
  public void onDateClick(@NonNull String id, @NonNull String screenName) {

    getView().openTweet(FormatUtils.getTweetUrl(id, screenName));
  }

  @Override
  public void onAvatarClick() {

    getView().openUser();
  }

  @Override
  public void onMediaClick() {

    getView().openMedia();
  }

  @Override
  public void onRetweetClick(@NonNull String id) {

    getView().toggleRetweet(id);
  }

  @Override
  public void onFavoriteClick(@NonNull String id) {

    getView().toggleFavorite(id);
  }
}
