package com.medo.tweetspie.base;

import android.support.annotation.StringRes;


public interface BaseView {

  CharSequence getText(@StringRes int textId);
}