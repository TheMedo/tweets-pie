package com.medo.tweetspie.system;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;


public class PreferencesInteractor implements PreferencesProvider {

  private final SharedPreferences preferences;

  public PreferencesInteractor(Context context) {

    preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
  }

  @Nullable
  @Override
  public String getString(@PreferenceKey String key) {

    return preferences.getString(key, null);
  }

  @Override
  public long getLong(@PreferenceKey String key) {

    return preferences.getLong(key, 0);
  }

  @Override
  public void set(@PreferenceKey String key, @Nullable String value) {

    preferences.edit().putString(key, value).apply();
  }

  @Override
  public void set(@PreferenceKey String key, long value) {

    preferences.edit().putLong(key, value).apply();
  }

  @Override
  public boolean has(@PreferenceKey String key) {

    return preferences.contains(key);
  }

  @Override
  public void remove(@PreferenceKey String key) {

    preferences.edit().remove(key).apply();
  }
}