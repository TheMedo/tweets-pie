package com.medo.tweetspie.rest;


import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.medo.tweetspie.consts.Constants;
import com.medo.tweetspie.database.RealmInteractor;
import com.medo.tweetspie.rest.api.CustomApiClient;
import com.medo.tweetspie.rest.model.FriendsIds;
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
import timber.log.Timber;


public class TwitterInteractor implements TwitterTransaction {

  private static final int TIMELINE_MAX_QUERIES = 3;

  private final PreferencesProvider preferences;
  private final RealmInteractor realmInteractor;
  private CustomApiClient customApiClient;

  public TwitterInteractor(PreferencesProvider preferences, RealmInteractor realmInteractor) {

    this.preferences = preferences;
    this.realmInteractor = realmInteractor;
  }

  public static void init(@NonNull Context context) {
    // init twitter
    TwitterAuthConfig authConfig = new TwitterAuthConfig(Constants.TWITTER_KEY, Constants.TWITTER_SECRET);
    Fabric.with(context, new TwitterCore(authConfig), new TweetUi());
  }

  private void checkInstance() {

    if (customApiClient == null) {
      // instantiate the custom api client if null
      customApiClient = new CustomApiClient(TwitterCore.getInstance().getSessionManager().getActiveSession());
    }
  }

  private void getFriendsIds(@Nullable Long beforeId) {

    checkInstance();
    customApiClient.getFriendsService().friends(
            null, preferences.get(PreferencesProvider.USERNAME), beforeId, false, 5000L, new Callback<FriendsIds>() {

              @Override
              public void success(Result<FriendsIds> result) {

                FriendsIds ids = result.data;
                if (ids == null) {
                  Timber.e("Cannot get friends ids: Empty response");
                  return;
                }
                // persist the friends ids
                realmInteractor.persistFriendsIds(ids.getIds());
                // get the next page of friend ids if any
                final long beforeId = ids.getPreviousCursor();
                if (beforeId != 0) {
                  getFriendsIds(beforeId);
                }
              }

              @Override
              public void failure(TwitterException exception) {
                // ignore exception
                Timber.e(exception, "Cannot get friends ids");
              }
            });
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

  @Override
  public boolean checkSession() {

    final boolean hasActiveSession = TwitterCore.getInstance().getSessionManager().getActiveSession() != null;
    if (!hasActiveSession) {
      preferences.remove(PreferencesProvider.USERNAME);
      return false;
    }
    if (!realmInteractor.hasFriendsIds()) {
      // fetch the friends ids if we have none
      getFriendsIds(-1L);
    }
    return true;
  }

  @Override
  public void getTimeline(@NonNull TweetsCallback callback) {

    getTimeline(1, null, callback);
  }
}
