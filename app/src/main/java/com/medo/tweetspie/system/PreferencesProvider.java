package com.medo.tweetspie.system;

import android.support.annotation.Nullable;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;


public interface PreferencesProvider {

  String USERNAME = "username";
  String RETWEETS = "retweets";
  String REPLIES = "replies";
  String LAST_UPDATE_TIMESTAMP = "lastUpdateTimestamp";
  String MAX_TWEETS = "maxTweets";

  void initWithDefaultValues();

  @Nullable
  String getString(@PreferenceKey String key);

  long getLong(@PreferenceKey String key);

  boolean getBoolean(@PreferenceKey String key);

  void set(@PreferenceKey String key, @Nullable String value);

  void set(@PreferenceKey String key, long value);

  void set(@PreferenceKey String key, boolean value);

  boolean has(@PreferenceKey String key);

  void remove(@PreferenceKey String key);

  @Retention(SOURCE)
  @StringDef({
          USERNAME,
          RETWEETS,
          REPLIES,
          LAST_UPDATE_TIMESTAMP,
          MAX_TWEETS
  })
  @interface PreferenceKey {}
}