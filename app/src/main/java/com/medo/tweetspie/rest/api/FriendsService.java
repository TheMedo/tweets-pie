package com.medo.tweetspie.rest.api;

import com.medo.tweetspie.rest.model.FriendsIds;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface FriendsService {

  @GET("/1.1/friends/ids.json")
  Call<FriendsIds> friends(@Query("user_id") Long id,
                           @Query("screen_name") String screenName,
                           @Query("cursor") Long cursor,
                           @Query("stringify_ids") Boolean stringifyIds,
                           @Query("count") Long count);
}