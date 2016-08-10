package com.medo.tweetspie.onboarding;

import com.medo.tweetspie.base.BasePresenter;
import com.medo.tweetspie.base.BaseView;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.TwitterSession;


public interface OnboardingContract {

  interface View extends BaseView {

    void setupTwitterButton(Callback<TwitterSession> callback);

    void exitWithSuccess();

    void exitWithError(String errorMessage);
  }


  interface Presenter extends BasePresenter<View> {

    void onLoginSuccess(TwitterSession twitterSession);

    void onLoginFailure(Exception e);
  }
}