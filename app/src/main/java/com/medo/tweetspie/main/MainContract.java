package com.medo.tweetspie.main;


import com.medo.tweetspie.base.BaseView;
import com.medo.tweetspie.base.BaseViewPresenter;
import com.medo.tweetspie.database.model.RealmTweet;

import io.realm.OrderedRealmCollection;


public interface MainContract {

  interface View extends BaseView {

    void startOnboarding();

    void initUi();

    void loadData();

    void showData(OrderedRealmCollection<RealmTweet> data);

    void showError();

    void exit();
  }


  interface Presenter extends BaseViewPresenter<View> {

    void onOnboardingSuccess();

    void onOnboardingFailure();

    void onDataLoaded(boolean success);
  }
}
