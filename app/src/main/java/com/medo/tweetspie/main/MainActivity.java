package com.medo.tweetspie.main;

import android.content.Intent;
import android.os.Bundle;

import com.medo.tweetspie.BaseActivity;
import com.medo.tweetspie.R;
import com.medo.tweetspie.bus.events.TimelineServiceEvent;
import com.medo.tweetspie.consts.Constants;
import com.medo.tweetspie.database.RealmInteractor;
import com.medo.tweetspie.onboarding.OnboardingActivity;
import com.medo.tweetspie.service.TimelineService;
import com.medo.tweetspie.system.PreferencesInteractor;
import com.medo.tweetspie.system.StringInteractor;
import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements MainContract.View {

  private MainContract.Actions presenter;
  private RealmInteractor realmInteractor;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    presenter = new MainPresenter(
            this,
            new PreferencesInteractor(this),
            new StringInteractor(this));
    realmInteractor = new RealmInteractor();

    presenter.onInitialize();
    realmInteractor.onInitialize();
  }

  @Override
  protected void onDestroy() {

    super.onDestroy();
    realmInteractor.onDestroy();
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
  public void showData() {

    // TODO show data
  }

  @Override
  public void showError() {

    // TODO show error
  }

  @Override
  public void exit() {

    finish();
  }

  @Subscribe
  public void onTimelineServiceResult(TimelineServiceEvent event) {

    presenter.onDataLoaded(event.isSuccess());
  }
}
