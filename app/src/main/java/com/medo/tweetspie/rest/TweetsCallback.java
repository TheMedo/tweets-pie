package com.medo.tweetspie.rest;


import android.support.annotation.NonNull;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;


public interface TweetsCallback extends TwitterCallback {

  void onTweetsAvailable(@NonNull List<Tweet> tweets);

  void onFinish();
}
