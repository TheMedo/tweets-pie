package com.medo.tweetspie.base;

import android.support.annotation.StringRes;


public interface BaseView {

  void initUi();

  void exit();

  CharSequence getText(@StringRes int textId);
}