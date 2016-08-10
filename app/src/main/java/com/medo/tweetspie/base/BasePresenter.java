package com.medo.tweetspie.base;

public interface BasePresenter <T extends BaseView> {

  void onAttachView(T vew);

  void onDetachView();
}