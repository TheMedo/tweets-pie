package com.medo.tweetspie.database;


import android.content.Context;
import android.support.annotation.NonNull;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;


public interface RealmTransaction {

  void init(@NonNull Context context);

  void open();

  void close();

  void persistTweets(@NonNull List<Tweet> tweets);
}
