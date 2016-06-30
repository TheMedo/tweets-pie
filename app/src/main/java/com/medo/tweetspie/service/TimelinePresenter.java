package com.medo.tweetspie.service;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.medo.tweetspie.rest.LoginCallback;
import com.medo.tweetspie.rest.TweetsCallback;
import com.medo.tweetspie.rest.TwitterTransaction;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import timber.log.Timber;


public class TimelinePresenter implements TimelineContract.Actions {


  private final TimelineContract.Service service;
  private final TwitterTransaction interactor;

  public TimelinePresenter(TimelineContract.Service service, TwitterTransaction interactor) {

    this.service = service;
    this.interactor = interactor;
  }

  @MainThread
  @Override
  public void onServiceStarted() {

    // notify service start
    service.notifyStart();
    // check for active session
    if (!interactor.hasActiveSession()) {
      // make a guest login
      interactor.loginGuest(new LoginCallback() {

        @Override
        public void onSuccess() {

          getTimeline();
        }

        @Override
        public void onError(@NonNull Exception e) {

          service.exitWithError(e);
        }
      });
    }
    else {
      // we have an active session, get the timeline
      getTimeline();
    }
  }

  @MainThread
  private void getTimeline() {

    interactor.getTimeline(new TweetsCallback() {

      @Override
      public void onTweetsAvailable(@NonNull List<Tweet> tweets) {

        persistTimelineTweets(rateTimelineTweets(tweets));
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

  @NonNull
  @MainThread
  private List<Tweet> rateTimelineTweets(@NonNull List<Tweet> timelineTweets) {
    // TODO rate the tweets
    return timelineTweets;
  }

  @MainThread
  private void persistTimelineTweets(@NonNull List<Tweet> timelineTweets) {
    // TODO persist the tweets
    Timber.d("Tweets persisted: %s", timelineTweets.size());
  }
}
