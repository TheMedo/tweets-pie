package com.medo.tweetspie.rest;


import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.medo.tweetspie.consts.Constants;
import com.medo.tweetspie.system.PreferencesProvider;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetUi;

import java.util.List;

import io.fabric.sdk.android.Fabric;


public class TwitterInteractor implements TwitterTransaction {

  private static final int TIMELINE_MAX_QUERIES = 3;

  private final PreferencesProvider preferences;

  public TwitterInteractor(PreferencesProvider preferences) {

    this.preferences = preferences;
  }

  @Override
  public void init(@NonNull Context context) {
    // init twitter
    TwitterAuthConfig authConfig = new TwitterAuthConfig(Constants.TWITTER_KEY, Constants.TWITTER_SECRET);
    Fabric.with(context, new TwitterCore(authConfig), new TweetUi());
  }

  @Override
  public boolean checkSession() {

    final boolean hasActiveSession = TwitterCore.getInstance().getSessionManager().getActiveSession() != null;
    if (!hasActiveSession) {
      preferences.remove(PreferencesProvider.USERNAME);
    }
    return hasActiveSession;
  }

  @Override
  public void getTimeline(@NonNull TweetsCallback callback) {

    getTimeline(1, null, callback);
  }

  private void getTimeline(@IntRange(from = 1, to = 3) final int part,
                           @Nullable Long maxId,
                           @NonNull final TweetsCallback callback) {

    TwitterCore.getInstance().getApiClient().getStatusesService().homeTimeline(
            200, null, maxId, false, true, false, false, new Callback<List<Tweet>>() {

              @Override
              public void success(Result<List<Tweet>> result) {

                if (result == null || result.data == null || result.data.isEmpty()) {
                  // finish if the response is null, we cannot process the data
                  callback.onFinish();
                  return;
                }

                // rate and persist the timeline tweets
                callback.onTweetsAvailable(result.data);
                if (part < TIMELINE_MAX_QUERIES) {
                  // recursively get more tweets from the user's timeline
                  // find out the lowest id to use as maxId
                  final Long newMaxId = result.data.get(result.data.size() - 1).id;
                  getTimeline(part + 1, newMaxId, callback);
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
            });
  }
}
