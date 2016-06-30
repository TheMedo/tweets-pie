package com.medo.tweetspie.onboarding;

import android.text.TextUtils;

import com.medo.tweetspie.R;
import com.medo.tweetspie.system.PreferencesProvider;
import com.medo.tweetspie.system.StringProvider;
import com.medo.tweetspie.utils.UsernameValidator;


public class OnboardingPresenter implements OnboardingContract.Actions {

  private final OnboardingContract.View view;
  private final PreferencesProvider preferences;
  private final StringProvider strings;

  public OnboardingPresenter(OnboardingContract.View view, PreferencesProvider preferencesProvider, StringProvider stringProvider) {

    this.view = view;
    this.preferences = preferencesProvider;
    this.strings = stringProvider;
  }

  @Override
  public void validateInputAndUpdateUi() {

    final String username = view.getUsername();
    if (TextUtils.isEmpty(username)) {
      // empty input, clear the error and disable the confirm button
      view.showError(null);
      view.enableConfirmButton(false);
      return;
    }
    if (!UsernameValidator.validate(username)) {
      // invalid input, set the error and disable the confirm button
      view.enableConfirmButton(false);
      view.showError(strings.getString(R.string.app_name));
      return;
    }
    // valid input, clear the error and enable the confirm button
    view.showError(null);
    view.enableConfirmButton(true);
  }

  @Override
  public void onViewInitialized() {
    // refresh ui when the view is initialized
    validateInputAndUpdateUi();
  }

  @Override
  public void onInputChanged() {
    // refresh ui on each user input
    validateInputAndUpdateUi();
  }

  @Override
  public void onConfirmButtonClick() {
    // save the username into the shared preferences
    final String username = view.getUsername();
    preferences.set(PreferencesProvider.USERNAME, username);
    // finish the activity with RESULT_OK
    view.finishWithSuccess();
  }
}
