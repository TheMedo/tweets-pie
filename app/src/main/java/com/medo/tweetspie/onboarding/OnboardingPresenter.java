package com.medo.tweetspie.onboarding;

import com.medo.tweetspie.R;
import com.medo.tweetspie.system.PreferencesProvider;
import com.medo.tweetspie.system.StringProvider;
import com.twitter.sdk.android.core.TwitterSession;

import timber.log.Timber;


public class OnboardingPresenter implements OnboardingContract.Actions {

  private final OnboardingContract.View view;
  private final PreferencesProvider preferences;
  private final StringProvider strings;

  public OnboardingPresenter(OnboardingContract.View view,
                             PreferencesProvider preferencesProvider,
                             StringProvider stringProvider) {

    this.view = view;
    this.preferences = preferencesProvider;
    this.strings = stringProvider;
  }

  @Override
  public void onLoginSuccess(TwitterSession twitterSession) {
    // persist the username and notify success
    preferences.set(PreferencesProvider.USERNAME, twitterSession.getUserName());
    view.exitWithSuccess();
  }

  @Override
  public void onLoginFailure(Exception e) {
    // log and notify error
    Timber.e(e, "Cannot login with Twitter");
    view.exitWithError(strings.getString(R.string.error_login_failure));
  }
}
