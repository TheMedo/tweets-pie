package com.medo.tweetspie.remote.api

import com.google.gson.annotations.SerializedName
import com.twitter.sdk.android.core.TwitterApiClient
import com.twitter.sdk.android.core.TwitterSession
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

class FriendsApiClient(session: TwitterSession) : TwitterApiClient(session) {

    val friendsService: FriendsService
        get() = getService(FriendsService::class.java)
}

interface FriendsService {

    @GET("/1.1/friends/ids.json")
    fun friends(
        @Query("screen_name") screenName: String,
        @Query("cursor") cursor: Long?,
        @Query("stringify_ids") stringifyIds: Boolean = true,
        @Query("count") count: Long = 2000L
    ): Call<FriendsIds>
}

data class FriendsIds(
    @SerializedName("previous_cursor") val previousCursor: Long?,
    @SerializedName("ids") val ids: List<String>,
    @SerializedName("next_cursor") val nextCursor: Long?
)