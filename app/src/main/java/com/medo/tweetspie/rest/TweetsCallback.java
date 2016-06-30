package com.medo.tweetspie.rest;


import android.support.annotation.NonNull;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;


public interface TweetsCallback {

  void onTweetsAvailable(@NonNull List<Tweet> tweets);

  void onFinish();

  void onError(@NonNull Exception e);
}
