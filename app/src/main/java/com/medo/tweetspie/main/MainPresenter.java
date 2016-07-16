package com.medo.tweetspie.main;

import com.medo.tweetspie.database.RealmInteractor;
import com.medo.tweetspie.database.model.RealmTweet;
import com.medo.tweetspie.system.PreferencesProvider;

import io.realm.OrderedRealmCollection;


public class MainPresenter implements MainContract.Actions {

  private final MainContract.View view;
  private final PreferencesProvider preferences;
  private final RealmInteractor realmInteractor;

  public MainPresenter(MainContract.View view,
                       PreferencesProvider preferencesProvider,
                       RealmInteractor realmInteractor) {

    this.view = view;
    this.preferences = preferencesProvider;
    this.realmInteractor = realmInteractor;
  }

  @Override
  public void onInitialize() {

    if (!preferences.has(PreferencesProvider.USERNAME)) {
      view.startOnboarding();
    }
    else {
      view.loadData();
      view.initUi();
      // show the persisted tweets if any
      OrderedRealmCollection<RealmTweet> tweets = realmInteractor.getTweets();
      if (!tweets.isEmpty()) {
        view.showData(tweets);
      }
    }
  }

  @Override
  public void onOnboardingSuccess() {

    view.loadData();
    view.initUi();
  }

  @Override
  public void onOnboardingFailure() {

    view.exit();
  }

  @Override
  public void onDataLoaded(boolean success) {

    if (success) {
      OrderedRealmCollection<RealmTweet> tweets = realmInteractor.getTweets();
      view.showData(tweets);
    }
    else {
      // TODO show error
      view.showError();
    }
  }
}
