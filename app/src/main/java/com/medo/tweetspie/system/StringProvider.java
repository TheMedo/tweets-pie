package com.medo.tweetspie.system;

import android.support.annotation.StringRes;


public interface StringProvider {

  String getString(@StringRes int stringId);

}