package com.medo.tweetspie.ui.main;

import com.medo.tweetspie.system.PreferencesProvider;
import com.medo.tweetspie.system.StringProvider;


public class MainPresenter implements MainContract.Actions {

  private final MainContract.View view;
  private final PreferencesProvider preferences;
  private final StringProvider strings;

  public MainPresenter(MainContract.View view, PreferencesProvider preferencesProvider, StringProvider stringProvider) {

    this.view = view;
    this.preferences = preferencesProvider;
    this.strings = stringProvider;
  }

  @Override
  public void onInitialize() {

    if (!preferences.has(PreferencesProvider.USERNAME)) {
      view.startOnboarding();
    }
    else {
      view.loadData();
      view.initUi();
    }
  }

  @Override
  public void onOnboardingSuccess() {

    view.loadData();
    view.initUi();
  }

  @Override
  public void onOnboardingFailure() {

    view.exit();
  }
}
