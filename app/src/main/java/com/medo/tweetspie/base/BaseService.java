package com.medo.tweetspie.base;

import android.support.annotation.StringRes;


public interface BaseService {

  CharSequence getText(@StringRes int textId);
}