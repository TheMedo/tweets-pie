package com.medo.tweetspie.onboarding;

import com.medo.tweetspie.base.BaseView;
import com.medo.tweetspie.base.BaseViewPresenter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.TwitterSession;


public interface OnboardingContract {

  interface View extends BaseView {

    void setupTwitterButton(Callback<TwitterSession> callback);

    void exitWithError(String errorMessage);
  }


  interface Presenter extends BaseViewPresenter<View> {

    void onLoginSuccess(TwitterSession twitterSession);

    void onLoginFailure(Exception e);
  }
}