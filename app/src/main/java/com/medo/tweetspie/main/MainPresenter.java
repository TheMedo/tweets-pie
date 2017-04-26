package com.medo.tweetspie.main;

import com.medo.tweetspie.base.AbsViewPresenter;
import com.medo.tweetspie.database.RealmInteractor;
import com.medo.tweetspie.database.model.RealmTweet;
import com.medo.tweetspie.system.PreferencesProvider;

import io.realm.OrderedRealmCollection;


class MainPresenter extends AbsViewPresenter<MainContract.View>
        implements MainContract.Presenter {

  private final PreferencesProvider preferences;
  private final RealmInteractor realmInteractor;

  MainPresenter(PreferencesProvider preferencesProvider, RealmInteractor realmInteractor) {

    this.preferences = preferencesProvider;
    this.realmInteractor = realmInteractor;
  }

  @Override
  public void onAttach(MainContract.View view) {

    super.onAttach(view);
    if (!preferences.has(PreferencesProvider.USERNAME)) {
      view.startOnboarding();
    }
    else {
      view.initUi();
      view.loadData();
      // show the persisted tweets if any
      OrderedRealmCollection<RealmTweet> tweets = realmInteractor.getTweets();
      if (!tweets.isEmpty()) {
        view.showData(tweets, true);
      }
    }
  }

  @Override
  public void onDetach() {

    super.onDetach();
    realmInteractor.onDestroy();
  }

  @Override
  public void onOnboardingSuccess() {

    if (view != null) {
      view.initUi();
      view.loadData();
    }
  }

  @Override
  public void onOnboardingFailure() {

    if (view != null) {
      view.exit();
    }
  }

  @Override
  public void onDataLoaded(boolean success) {

    if (view != null) {
      if (success) {
        OrderedRealmCollection<RealmTweet> tweets = realmInteractor.getTweets();
        view.showData(tweets, false);
      }
      else {
        // TODO show error
        view.showError();
      }
    }
  }
}
