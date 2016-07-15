package com.medo.tweetspie.main;


import com.medo.tweetspie.database.model.RealmTweet;

import io.realm.OrderedRealmCollection;


public interface MainContract {

  interface View {

    void startOnboarding();

    void initUi();

    void loadData();

    void showData(OrderedRealmCollection<RealmTweet> data);

    void showError();

    void exit();
  }


  interface Actions {

    void onInitialize();

    void onOnboardingSuccess();

    void onOnboardingFailure();

    void onDataLoaded(boolean success);
  }
}
