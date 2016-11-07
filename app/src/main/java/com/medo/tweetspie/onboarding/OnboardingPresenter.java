package com.medo.tweetspie.onboarding;

import com.medo.tweetspie.R;
import com.medo.tweetspie.base.AbsViewPresenter;
import com.medo.tweetspie.system.PreferencesInteractor;
import com.medo.tweetspie.system.PreferencesProvider;
import com.medo.tweetspie.system.StringInteractor;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

import timber.log.Timber;


public class OnboardingPresenter extends AbsViewPresenter<OnboardingContract.View>
        implements OnboardingContract.Presenter {

  private final PreferencesInteractor preferences;
  private final StringInteractor strings;

  public OnboardingPresenter(PreferencesInteractor preferences,
                             StringInteractor strings) {

    this.preferences = preferences;
    this.strings = strings;
  }

  @Override
  public void onAttach(OnboardingContract.View view) {

    super.onAttach(view);
    getView().initUi();
    getView().setupTwitterButton(new Callback<TwitterSession>() {

      @Override
      public void success(Result<TwitterSession> result) {

        onLoginSuccess(result.data);
      }

      @Override
      public void failure(TwitterException exception) {

        onLoginFailure(exception);
      }
    });
  }

  @Override
  public void onLoginSuccess(TwitterSession twitterSession) {
    // persist the username and notify success
    preferences.initWithDefaultValues();
    preferences.set(PreferencesProvider.USERNAME, twitterSession.getUserName());
    getView().exit();
  }

  @Override
  public void onLoginFailure(Exception e) {
    // log and notify error
    Timber.e(e, "Cannot login with Twitter");
    getView().exitWithError(strings.getString(R.string.error_login_failure));
  }
}
