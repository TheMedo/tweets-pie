package com.medo.tweetspie.onboarding;

public interface OnboardingContract {

  interface View {

    String getUsername();

    void enableConfirmButton(boolean enabled);

    void showError(String message);

    void finishWithSuccess();

  }


  interface Actions {

    void validateInputAndUpdateUi();

    void onViewInitialized();

    void onInputChanged();

    void onConfirmButtonClick();

  }
}