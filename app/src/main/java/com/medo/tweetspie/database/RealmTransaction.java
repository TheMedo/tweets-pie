package com.medo.tweetspie.database;


import android.support.annotation.NonNull;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;


public interface RealmTransaction {

  void onDestroy();

  void persistTweets(@NonNull List<Tweet> tweets);

  void persistFriendsIds(@NonNull List<Long> friendsIds);

  boolean hasFriendsIds();

  boolean toggleRetweet(@NonNull String id);

  boolean toggleFavorite(@NonNull String id);
}
