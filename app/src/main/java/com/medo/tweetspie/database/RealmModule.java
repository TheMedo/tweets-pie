package com.medo.tweetspie.database;

import com.medo.tweetspie.database.utils.RealmConverter;
import com.medo.tweetspie.injection.scopes.UserScope;
import com.medo.tweetspie.system.PreferencesInteractor;

import dagger.Module;
import dagger.Provides;


@Module
public class RealmModule {

  @Provides
  @UserScope
  RealmInteractor provideRealm(PreferencesInteractor preferences, RealmConverter converter) {

    return new RealmInteractor(preferences, converter);
  }
}
