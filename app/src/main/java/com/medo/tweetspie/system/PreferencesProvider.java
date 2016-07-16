package com.medo.tweetspie.system;

import android.support.annotation.Nullable;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;


public interface PreferencesProvider {

  String USERNAME = "username";

  @Nullable
  String get(@PreferenceKey String key);

  void set(@PreferenceKey String key, @Nullable String value);

  boolean has(@PreferenceKey String key);

  void remove(@PreferenceKey String key);

  @Retention(SOURCE)
  @StringDef({
          USERNAME
  })
  @interface PreferenceKey {}
}