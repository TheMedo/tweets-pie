package com.medo.tweetspie.service;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.medo.tweetspie.base.AbsServicePresenter;
import com.medo.tweetspie.database.RealmTransaction;
import com.medo.tweetspie.rest.TweetsCallback;
import com.medo.tweetspie.rest.TwitterTransaction;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import timber.log.Timber;


public class TimelinePresenter extends AbsServicePresenter<TimelineContract.Service>
        implements TimelineContract.Presenter {


  private final TwitterTransaction twitterInteractor;
  private final RealmTransaction realmInteractor;

  public TimelinePresenter(TwitterTransaction twitterInteractor,
                           RealmTransaction realmInteractor) {

    this.twitterInteractor = twitterInteractor;
    this.realmInteractor = realmInteractor;
  }

  @MainThread
  @Override
  public void onStart(TimelineContract.Service service) {

    super.onStart(service);
    if (twitterInteractor.checkSession()) {
      // we have an active session, get the timeline
      getTimeline();
    }
  }

  @MainThread
  private void getTimeline() {

    twitterInteractor.getTimeline(new TweetsCallback() {

      @Override
      public void onTweetsAvailable(@NonNull List<Tweet> tweets) {
        // persist the obtained tweets
        Timber.d("Obtained %d tweets", tweets.size());
        realmInteractor.persistTweets(tweets);
      }

      @Override
      public void onFinish() {

        getService().exitWithSuccess();
      }

      @Override
      public void onError(@NonNull Exception e) {

        getService().exitWithError(e);
      }
    });
  }
}
