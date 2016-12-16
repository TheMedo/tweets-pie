package com.medo.tweetspie.main;

import com.medo.tweetspie.base.AbsViewPresenter;
import com.medo.tweetspie.database.RealmInteractor;
import com.medo.tweetspie.database.model.RealmTweet;
import com.medo.tweetspie.system.PreferencesProvider;

import io.realm.OrderedRealmCollection;


public class MainPresenter extends AbsViewPresenter<MainContract.View>
        implements MainContract.Presenter {

  private final PreferencesProvider preferences;
  private final RealmInteractor realmInteractor;

  public MainPresenter(PreferencesProvider preferencesProvider,
                       RealmInteractor realmInteractor) {

    this.preferences = preferencesProvider;
    this.realmInteractor = realmInteractor;
  }

  @Override
  public void onAttach(MainContract.View view) {

    super.onAttach(view);
    if (!preferences.has(PreferencesProvider.USERNAME)) {
      getView().startOnboarding();
    }
    else {
      getView().initUi();
      getView().loadData();
      // show the persisted tweets if any
      OrderedRealmCollection<RealmTweet> tweets = realmInteractor.getTweets();
      if (!tweets.isEmpty()) {
        getView().showData(tweets);
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

    getView().initUi();
    getView().loadData();
  }

  @Override
  public void onOnboardingFailure() {

    getView().exit();
  }

  @Override
  public void onDataLoaded(boolean success) {

    if (success) {
      OrderedRealmCollection<RealmTweet> tweets = realmInteractor.getTweets();
      getView().showData(tweets);
    }
    else {
      // TODO show error
      getView().showError();
    }
  }
}
