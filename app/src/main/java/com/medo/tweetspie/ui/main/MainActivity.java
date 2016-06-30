package com.medo.tweetspie.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.medo.tweetspie.R;
import com.medo.tweetspie.consts.Constants;
import com.medo.tweetspie.service.TimelineService;
import com.medo.tweetspie.system.PreferencesInteractor;
import com.medo.tweetspie.system.StringInteractor;
import com.medo.tweetspie.ui.onboarding.OnboardingActivity;

import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements MainContract.View {

  private MainContract.Actions presenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    presenter = new MainPresenter(
            this,
            new PreferencesInteractor(this),
            new StringInteractor(this));
    presenter.onInitialize();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == Constants.REQUEST_CODE_ONBOARDING) {
      if (resultCode == RESULT_OK) {
        presenter.onOnboardingSuccess();
      }
      else {
        presenter.onOnboardingFailure();
      }
    }
  }

  @Override
  public void startOnboarding() {

    startActivityForResult(OnboardingActivity.getIntent(this), Constants.REQUEST_CODE_ONBOARDING);
  }

  @Override
  public void initUi() {

    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
  }

  @Override
  public void loadData() {

    TimelineService.start(this);
  }

  @Override
  public void exit() {

    finish();
  }
}
