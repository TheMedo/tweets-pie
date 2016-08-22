package com.medo.tweetspie.rest;


import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.medo.tweetspie.BuildConfig;
import com.medo.tweetspie.database.RealmInteractor;
import com.medo.tweetspie.rest.api.CustomApiClient;
import com.medo.tweetspie.rest.model.FriendsIds;
import com.medo.tweetspie.system.PreferencesProvider;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetUi;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;


public class TwitterInteractor implements TwitterTransaction {

  private static final int TIMELINE_MAX_QUERIES = 1;
  private static final long TIMELINE_REFRESH_INTERVAL = TimeUnit.MINUTES.toMillis(60);

  private final PreferencesProvider preferences;
  private final RealmInteractor realmInteractor;
  private final TwitterApiClient apiClient;
  private final CustomApiClient customApiClient;

  public TwitterInteractor(PreferencesProvider preferences, RealmInteractor realmInteractor) {

    this.preferences = preferences;
    this.realmInteractor = realmInteractor;
    this.apiClient = TwitterCore.getInstance().getApiClient();
    this.customApiClient = new CustomApiClient(TwitterCore.getInstance().getSessionManager().getActiveSession());
  }

  public static void init(@NonNull Context context) {
    // init twitter
    TwitterAuthConfig authConfig = new TwitterAuthConfig(BuildConfig.TWITTER_KEY, BuildConfig.TWITTER_SECRET);
    Fabric.with(context, new TwitterCore(authConfig), new TweetUi());
  }

  private void getFriendsIds(@Nullable Long beforeId) {

    customApiClient.getFriendsService().friends(
            null, preferences.getString(PreferencesProvider.USERNAME), beforeId, false, 5000L, new Callback<FriendsIds>() {

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

    long lastUpdateTimestamp = preferences.getLong(PreferencesProvider.LAST_UPDATE_TIMESTAMP);
    if (System.currentTimeMillis() - lastUpdateTimestamp < TIMELINE_REFRESH_INTERVAL) {
      // don't trigger network request all too often
      callback.onFinish();
      return;
    }

    apiClient.getStatusesService().homeTimeline(
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
                  preferences.set(PreferencesProvider.LAST_UPDATE_TIMESTAMP, System.currentTimeMillis());
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

  @Override
  public void retweetStatus(@NonNull String tweetId, boolean retweet, @Nullable final TwitterCallback callback) {

    final Callback<Tweet> tweetCallback = new Callback<Tweet>() {

      @Override
      public void success(Result<Tweet> result) {

        if (callback != null) {
          callback.onSuccess();
        }
      }

      @Override
      public void failure(TwitterException exception) {

        if (callback != null) {
          callback.onError(exception);
        }
      }
    };

    if (retweet) {
      // retweet the status
      apiClient.getStatusesService().retweet(Long.valueOf(tweetId), false, tweetCallback);
    }
    else {
      // undo the retweet
      apiClient.getStatusesService().unretweet(Long.valueOf(tweetId), false, tweetCallback);
    }
  }

  @Override
  public void favoriteStatus(@NonNull String tweetId, boolean favorite, @Nullable final TwitterCallback callback) {

    final Callback<Tweet> tweetCallback = new Callback<Tweet>() {

      @Override
      public void success(Result<Tweet> result) {

        if (callback != null) {
          callback.onSuccess();
        }
      }

      @Override
      public void failure(TwitterException exception) {

        if (callback != null) {
          callback.onError(exception);
        }
      }
    };

    if (favorite) {
      // favorite the status
      apiClient.getFavoriteService().create(Long.valueOf(tweetId), false, tweetCallback);
    }
    else {
      // undo the favorite
      apiClient.getFavoriteService().destroy(Long.valueOf(tweetId), false, tweetCallback);
    }
  }
}
