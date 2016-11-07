package com.medo.tweetspie.main;

import com.medo.tweetspie.database.RealmInteractor;
import com.medo.tweetspie.injection.scopes.UserScope;
import com.medo.tweetspie.main.adapter.AdapterPresenter;
import com.medo.tweetspie.rest.TwitterInteractor;
import com.medo.tweetspie.service.TimelinePresenter;
import com.medo.tweetspie.system.PreferencesInteractor;

import dagger.Module;
import dagger.Provides;


@Module
public class MainModule {

  @Provides
  @UserScope
  MainPresenter provideMainPresenter(PreferencesInteractor preferences, RealmInteractor realm) {

    return new MainPresenter(preferences, realm);
  }

  @Provides
  @UserScope
  AdapterPresenter provideAdapterPresenter(TwitterInteractor twitterInteractor, RealmInteractor realm) {

    return new AdapterPresenter(twitterInteractor, realm);
  }

  @Provides
  @UserScope
  TimelinePresenter provideTimelinePresenter(TwitterInteractor twitterInteractor, RealmInteractor realm) {

    return new TimelinePresenter(twitterInteractor, realm);
  }
}
