package com.medo.tweetspie.service;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.medo.tweetspie.database.RealmTransaction;
import com.medo.tweetspie.rest.TweetsCallback;
import com.medo.tweetspie.rest.TwitterTransaction;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import timber.log.Timber;


public class TimelinePresenter implements TimelineContract.Actions {


  private final TimelineContract.Service service;
  private final TwitterTransaction twitterInteractor;
  private final RealmTransaction realmInteractor;

  public TimelinePresenter(TimelineContract.Service service,
                           TwitterTransaction twitterInteractor,
                           RealmTransaction realmInteractor) {

    this.service = service;
    this.twitterInteractor = twitterInteractor;
    this.realmInteractor = realmInteractor;
  }

  @MainThread
  @Override
  public void onServiceStarted() {

    // notify service start
    service.notifyStart();
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

        service.exitWithSuccess();
      }

      @Override
      public void onError(@NonNull Exception e) {

        service.exitWithError(e);
      }
    });
  }
}
