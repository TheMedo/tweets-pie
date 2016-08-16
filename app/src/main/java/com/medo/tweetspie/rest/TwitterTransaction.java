package com.medo.tweetspie.rest;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public interface TwitterTransaction {

  boolean checkSession();

  void getTimeline(@NonNull TweetsCallback callback);

  void retweetStatus(@NonNull String tweetId, boolean retweet, @Nullable TwitterCallback callback);

  void favoriteStatus(@NonNull String tweetId, boolean favorite, @Nullable TwitterCallback callback);
}
