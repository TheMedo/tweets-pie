package com.medo.tweetspie.rest;


import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.medo.tweetspie.consts.Constants;
import com.medo.tweetspie.system.PreferencesProvider;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.GuestCallback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetUi;
import com.twitter.sdk.android.tweetui.UserTimeline;

import io.fabric.sdk.android.Fabric;


public class TwitterInteractor implements TwitterTransaction {

  private static final int TIMELINE_MAX_QUERIES = 3;

  private final UserTimeline timeline;

  public TwitterInteractor() {

    this.timeline = null;
  }

  public TwitterInteractor(PreferencesProvider preferences) {

    this.timeline = new UserTimeline.Builder()
            .includeReplies(preferences.has(PreferencesProvider.REPLIES))
            .includeRetweets(preferences.has(PreferencesProvider.RETWEETS))
            .maxItemsPerRequest(200)
            .screenName(preferences.get(PreferencesProvider.USERNAME))
            .build();
  }

  @Override
  public void init(@NonNull Context context) {
    // init twitter
    TwitterAuthConfig authConfig = new TwitterAuthConfig(Constants.TWITTER_KEY, Constants.TWITTER_SECRET);
    Fabric.with(context, new TwitterCore(authConfig), new TweetUi());
  }

  @Override
  public boolean hasActiveSession() {

    return TwitterCore.getInstance().getAppSessionManager().getActiveSession() != null;
  }

  @Override
  public void loginGuest(@NonNull final LoginCallback callback) {

    TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {

      @Override
      public void success(Result<AppSession> result) {

        callback.onSuccess();
      }

      @Override
      public void failure(TwitterException exception) {

        callback.onError(exception);
      }
    });
  }

  @Override
  public void getTimeline(@NonNull TweetsCallback callback) {

    getTimeline(1, null, callback);
  }

  private void getTimeline(@IntRange(from = 1, to = 3) final int part,
                           @Nullable Long beforeId,
                           @NonNull final TweetsCallback callback) {

    if (timeline == null) {
      callback.onError(new Exception("Timeline is null"));
      return;
    }
    timeline.previous(beforeId, new GuestCallback<>(new Callback<TimelineResult<Tweet>>() {

      @Override
      public void success(Result<TimelineResult<Tweet>> result) {

        if (result == null || result.data == null || result.data.items == null || result.data.timelineCursor == null) {
          // finish if the response is null, we cannot process the data
          callback.onFinish();
          return;
        }

        // rate and persist the timeline tweets
        callback.onTweetsAvailable(result.data.items);
        if (part < TIMELINE_MAX_QUERIES) {
          // recursively get more tweets from the user's timeline
          // find out the lowest id to use as beforeId
          final Long newBeforeId = result.data.timelineCursor.minPosition;
          getTimeline(part + 1, newBeforeId, callback);
        }
        else {
          // complete the service once all tweets from user's timeline
          // have been processed and persisted
          callback.onFinish();
        }
      }

      @Override
      public void failure(TwitterException exception) {
        // exit with error on failure
        callback.onError(exception);
      }
    }));
  }
}
