package com.medo.tweetspie.base;

public interface BaseServicePresenter <T extends BaseService> {

  void onStart(T service);

  void onStop();
}