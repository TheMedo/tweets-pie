package com.medo.tweetspie.base;

public interface BaseViewPresenter <T extends BaseView> {

  void onAttach(T view);

  void onDetach();
}