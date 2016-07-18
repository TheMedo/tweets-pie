package com.medo.tweetspie.rest.api;

import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;


public class CustomApiClient extends TwitterApiClient {

  public CustomApiClient(TwitterSession session) {

    super(session);
  }

  /**
   * Provide CustomService with defined endpoints
   */
  public FriendsService getFriendsService() {

    return getService(FriendsService.class);
  }
}