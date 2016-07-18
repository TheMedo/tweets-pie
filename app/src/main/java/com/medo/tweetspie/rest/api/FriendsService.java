package com.medo.tweetspie.rest.api;

import com.medo.tweetspie.rest.model.FriendsIds;
import com.twitter.sdk.android.core.Callback;

import retrofit.http.GET;
import retrofit.http.Query;


public interface FriendsService {

  @GET("/1.1/friends/ids.json")
  void friends(@Query("user_id") Long id,
               @Query("screen_name") String screenName,
               @Query("cursor") Long cursor,
               @Query("stringify_ids") Boolean stringifyIds,
               @Query("count") Long count,
               Callback<FriendsIds> callback);
}