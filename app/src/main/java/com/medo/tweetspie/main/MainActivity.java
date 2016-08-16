package com.medo.tweetspie.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.medo.tweetspie.R;
import com.medo.tweetspie.base.BaseActivity;
import com.medo.tweetspie.bus.events.TimelineServiceEvent;
import com.medo.tweetspie.database.RealmInteractor;
import com.medo.tweetspie.database.model.RealmTweet;
import com.medo.tweetspie.main.adapter.AdapterContract;
import com.medo.tweetspie.main.adapter.AdapterPresenter;
import com.medo.tweetspie.main.adapter.TweetsAdapter;
import com.medo.tweetspie.onboarding.OnboardingActivity;
import com.medo.tweetspie.service.TimelineService;
import com.medo.tweetspie.system.PreferencesInteractor;
import com.medo.tweetspie.utils.DividerItemDecoration;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;


public class MainActivity extends BaseActivity implements MainContract.View, AdapterContract.View {

  private static final int REQUEST_CODE_ONBOARDING = 1620;

  @BindView(R.id.recycler_tweets)
  RecyclerView recyclerTweets;

  private MainContract.Presenter presenter;
  private AdapterContract.Presenter adapterPresenter;
  private RealmInteractor realmInteractor;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    PreferencesInteractor preferences = new PreferencesInteractor(this);
    realmInteractor = new RealmInteractor(preferences);
    presenter = new MainPresenter(preferences, realmInteractor);
    presenter.onAttach(this);
    adapterPresenter = new AdapterPresenter();
    adapterPresenter.onAttach(this);
  }

  @Override
  protected void onDestroy() {

    super.onDestroy();
    realmInteractor.onDestroy();
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
  public void initUi() {

    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
  }

  @Override
  public void loadData() {

    TimelineService.start(this);
  }

  @Override
  public void showData(OrderedRealmCollection<RealmTweet> tweets) {

    TweetsAdapter tweetsAdapter = (TweetsAdapter) recyclerTweets.getAdapter();
    if (tweetsAdapter != null) {
      // just update the data if the adapter has already been set
      tweetsAdapter.updateData(tweets);
      return;
    }
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
  public void openMedia() {

  }

  @Override
  public void openUser() {

  }

  @Override
  public void openTweet(@NonNull String url) {

    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    startActivity(intent);
  }

  @Override
  public void toggleRetweet(@NonNull String id) {

    realmInteractor.toggleRetweet(id);
  }

  @Override
  public void toggleFavorite(@NonNull String id) {

    realmInteractor.toggleFavorite(id);
  }

  @Subscribe
  public void onTimelineServiceResult(TimelineServiceEvent event) {

    presenter.onDataLoaded(event.isSuccess());
  }
}
