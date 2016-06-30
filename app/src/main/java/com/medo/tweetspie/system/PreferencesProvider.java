package com.medo.tweetspie.system;

import android.support.annotation.Nullable;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;


public interface PreferencesProvider {

  String USERNAME = "username";
  String RETWEETS = "retweets";
  String REPLIES = "replies";

  @Nullable
  String get(@PreferenceKey String key);

  void set(@PreferenceKey String key, @Nullable String value);

  boolean has(@PreferenceKey String key);

  @Retention(SOURCE)
  @StringDef({
          USERNAME,
          RETWEETS,
          REPLIES
  })
  @interface PreferenceKey {}
}