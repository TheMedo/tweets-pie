package com.medo.tweetspie.rest;

import com.medo.tweetspie.database.RealmInteractor;
import com.medo.tweetspie.injection.scopes.UserScope;
import com.medo.tweetspie.system.PreferencesInteractor;

import dagger.Module;
import dagger.Provides;


@Module
public class TwitterModule {

  @Provides
  @UserScope
  TwitterInteractor provideTwitter(PreferencesInteractor preferences, RealmInteractor realm) {

    return new TwitterInteractor(preferences, realm);
  }
}
