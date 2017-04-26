package com.medo.tweetspie.main;


import com.medo.tweetspie.base.BaseView;
import com.medo.tweetspie.base.BaseViewPresenter;
import com.medo.tweetspie.database.model.RealmTweet;

import io.realm.OrderedRealmCollection;


interface MainContract {

  interface View extends BaseView {

    void startOnboarding();

    void loadData();

    void showData(OrderedRealmCollection<RealmTweet> data, boolean cached);

    void showError();
  }


  interface Presenter extends BaseViewPresenter<View> {

    void onOnboardingSuccess();

    void onOnboardingFailure();

    void onDataLoaded(boolean success);
  }
}
