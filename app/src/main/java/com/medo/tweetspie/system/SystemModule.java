package com.medo.tweetspie.system;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class SystemModule {

  @Provides
  @Singleton
  PreferencesInteractor providePreferences(Application application) {

    return new PreferencesInteractor(application);
  }

  @Provides
  @Singleton
  StringInteractor provideStrings(Application application) {

    return new StringInteractor(application);
  }
}
