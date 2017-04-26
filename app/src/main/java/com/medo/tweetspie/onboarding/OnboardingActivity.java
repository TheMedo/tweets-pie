package com.medo.tweetspie.onboarding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.medo.tweetspie.R;
import com.medo.tweetspie.base.BaseActivity;
import com.medo.tweetspie.injection.components.AppComponent;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OnboardingActivity extends BaseActivity implements OnboardingContract.View {

  @BindView(R.id.login_button)
  TwitterLoginButton loginButton;

  @Inject
  OnboardingPresenter presenter;

  @NonNull
  public static Intent getIntent(@NonNull Activity parent) {

    return new Intent(parent, OnboardingActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    presenter.onAttach(this);
  }

  @Override
  protected void onDestroy() {

    super.onDestroy();
    presenter.onDetach();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    // Pass the activity result to the login button.
    super.onActivityResult(requestCode, resultCode, data);
    loginButton.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  protected void inject(@NonNull AppComponent appComponent) {

    DaggerOnboardingComponent.builder()
            .appComponent(appComponent)
            .onboardingModule(new OnboardingModule())
            .build()
            .inject(this);
  }

  @Override
  public void initUi() {

    setContentView(R.layout.activity_onboarding);
    ButterKnife.bind(this);
  }

  @Override
  public void setupTwitterButton(Callback<TwitterSession> callback) {

    loginButton.setCallback(callback);
  }

  @Override
  public void exit() {

    setResult(RESULT_OK);
    finish();
  }

  @Override
  public void exitWithError(String errorMessage) {

    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    setResult(RESULT_CANCELED);
    finish();
  }
}
