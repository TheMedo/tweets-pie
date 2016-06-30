package com.medo.tweetspie.system;

import android.content.Context;
import android.support.annotation.StringRes;


public class StringInteractor implements StringProvider {

  private final Context context;

  public StringInteractor(Context context) {

    this.context = context.getApplicationContext();
  }

  @Override
  public String getString(@StringRes int stringId) {

    return context.getString(stringId);
  }

}