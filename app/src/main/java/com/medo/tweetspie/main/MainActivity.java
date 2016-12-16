package com.medo.tweetspie.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.medo.tweetspie.R;
import com.medo.tweetspie.base.BaseActivity;
import com.medo.tweetspie.bus.events.TimelineServiceEvent;
import com.medo.tweetspie.database.RealmModule;
import com.medo.tweetspie.database.model.RealmTweet;
import com.medo.tweetspie.injection.components.AppComponent;
import com.medo.tweetspie.injection.components.DaggerUserComponent;
import com.medo.tweetspie.main.adapter.AdapterContract;
import com.medo.tweetspie.main.adapter.AdapterPresenter;
import com.medo.tweetspie.main.adapter.TweetsAdapter;
import com.medo.tweetspie.main.viewer.ViewerActivity;
import com.medo.tweetspie.onboarding.OnboardingActivity;
import com.medo.tweetspie.rest.TwitterModule;
import com.medo.tweetspie.service.TimelineService;
import com.medo.tweetspie.utils.DividerItemDecoration;
import com.medo.tweetspie.utils.IntentUtils;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;


public class MainActivity extends BaseActivity implements MainContract.View, AdapterContract.View {

  private static final int REQUEST_CODE_ONBOARDING = 1620;

  @BindView(R.id.recycler_tweets)
  RecyclerView recyclerTweets;
  @BindView(R.id.progress)
  ProgressBar progress;

  @Inject
  MainPresenter presenter;
  @Inject
  AdapterPresenter adapterPresenter;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    presenter.onAttach(this);
  }

  @Override
  protected void onDestroy() {

    super.onDestroy();
    presenter.onDetach();
    adapterPresenter.onDetach();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE_ONBOARDING) {
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

    startActivityForResult(OnboardingActivity.getIntent(this), REQUEST_CODE_ONBOARDING);
  }

  @Override
  protected void inject(@NonNull AppComponent appComponent) {

    DaggerUserComponent.builder()
            .appComponent(appComponent)
            .mainModule(new MainModule())
            .realmModule(new RealmModule())
            .twitterModule(new TwitterModule())
            .build()
            .inject(this);
  }

  @Override
  public void initUi() {

    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
  }

  @Override
  public void loadData() {

    // show progress bar and start loading data
    progress.setVisibility(View.VISIBLE);
    TimelineService.start(this);
  }

  @Override
  public void showData(OrderedRealmCollection<RealmTweet> tweets) {

    // hide the progress bar
    progress.setVisibility(View.GONE);

    // create an adapter for displaying tweets
    TweetsAdapter tweetsAdapter = (TweetsAdapter) recyclerTweets.getAdapter();
    if (tweetsAdapter != null) {
      // just update the data if the adapter has already been set
      tweetsAdapter.updateData(tweets);
      return;
    }

    adapterPresenter.onAttach(this);
    // setup the recycler view and set the adapter
    tweetsAdapter = new TweetsAdapter(this, tweets, adapterPresenter);
    recyclerTweets.setLayoutManager(new LinearLayoutManager(this));
    recyclerTweets.setAdapter(tweetsAdapter);
    recyclerTweets.setHasFixedSize(true);
    recyclerTweets.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
  }

  @Override
  public void showError() {

    // TODO show error
  }

  @Override
  public void exit() {

    finish();
  }

  @Override
  public void openTweetMedia(@NonNull String tweetId) {

    final Intent intent = ViewerActivity.getIntent(this, tweetId);
    startActivity(intent);
  }

  @Override
  public void openUrl(@NonNull String url) {

    IntentUtils.openUrl(this, url);
  }

  @Subscribe
  public void onTimelineServiceResult(TimelineServiceEvent event) {

    presenter.onDataLoaded(event.isSuccess());
  }
}
