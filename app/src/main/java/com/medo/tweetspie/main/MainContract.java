package com.medo.tweetspie.main;


public interface MainContract {

  interface View {

    void startOnboarding();

    void initUi();

    void loadData();

    void exit();
  }


  interface Actions {

    void onInitialize();

    void onOnboardingSuccess();

    void onOnboardingFailure();
  }
}
