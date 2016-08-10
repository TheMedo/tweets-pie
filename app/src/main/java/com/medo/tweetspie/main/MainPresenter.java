package com.medo.tweetspie.main;

import com.medo.tweetspie.base.AbsPresenter;
import com.medo.tweetspie.database.RealmInteractor;
import com.medo.tweetspie.database.model.RealmTweet;
import com.medo.tweetspie.system.PreferencesProvider;

import io.realm.OrderedRealmCollection;


public class MainPresenter extends AbsPresenter<MainContract.View>
        implements MainContract.Presenter {

  private final PreferencesProvider preferences;
  private final RealmInteractor realmInteractor;

  public MainPresenter(PreferencesProvider preferencesProvider,
                       RealmInteractor realmInteractor) {

    this.preferences = preferencesProvider;
    this.realmInteractor = realmInteractor;
  }

  @Override
  public void onAttachView(MainContract.View view) {

    super.onAttachView(view);
    if (!preferences.has(PreferencesProvider.USERNAME)) {
      getView().startOnboarding();
    }
    else {
      getView().loadData();
      getView().initUi();
      // show the persisted tweets if any
      OrderedRealmCollection<RealmTweet> tweets = realmInteractor.getTweets();
      if (!tweets.isEmpty()) {
        getView().showData(tweets);
      }
    }
  }

  @Override
  public void onDetachView() {

    super.onDetachView();
  }

  @Override
  public void onOnboardingSuccess() {

    getView().loadData();
    getView().initUi();
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
