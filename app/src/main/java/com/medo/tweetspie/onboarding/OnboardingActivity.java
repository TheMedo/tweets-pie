package com.medo.tweetspie.onboarding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.medo.tweetspie.R;
import com.medo.tweetspie.base.BaseActivity;
import com.medo.tweetspie.system.PreferencesInteractor;
import com.medo.tweetspie.system.StringInteractor;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OnboardingActivity extends BaseActivity implements OnboardingContract.View {

  @BindView(R.id.login_button)
  TwitterLoginButton loginButton;

  private OnboardingContract.Presenter presenter;

  @NonNull
  public static Intent getIntent(@NonNull Activity parent) {

    return new Intent(parent, OnboardingActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_onboarding);
    ButterKnife.bind(this);

    presenter = new OnboardingPresenter(new PreferencesInteractor(this), new StringInteractor(this));
    presenter.onAttachView(this);
  }

  @Override
  protected void onDestroy() {

    super.onDestroy();
    presenter.onDetachView();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    // Pass the activity result to the login button.
    super.onActivityResult(requestCode, resultCode, data);
    loginButton.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  public void setupTwitterButton(Callback<TwitterSession> callback) {

    loginButton.setCallback(callback);
  }

  @Override
  public void exitWithSuccess() {

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
