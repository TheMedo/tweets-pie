package com.medo.tweetspie.onboarding;

import com.twitter.sdk.android.core.TwitterSession;


public interface OnboardingContract {

  interface View {

    void exitWithSuccess();

    void exitWithError(String errorMessage);
  }


  interface Actions {

    void onLoginSuccess(TwitterSession twitterSession);

    void onLoginFailure(Exception e);
  }
}