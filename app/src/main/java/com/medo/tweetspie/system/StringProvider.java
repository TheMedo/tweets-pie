package com.medo.tweetspie.system;

import android.support.annotation.StringRes;


interface StringProvider {

  String getString(@StringRes int stringId);

}