package com.medo.tweetspie.database;

import com.medo.tweetspie.injection.scopes.UserScope;
import com.medo.tweetspie.system.PreferencesInteractor;

import dagger.Module;
import dagger.Provides;


@Module
public class RealmModule {

  @Provides
  @UserScope
  RealmInteractor provideRealm(PreferencesInteractor preferences) {

    return new RealmInteractor(preferences);
  }
}
