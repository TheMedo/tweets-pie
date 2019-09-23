package com.medo.tweetspie.remote.api

import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.services.FavoriteService
import com.twitter.sdk.android.core.services.StatusesService

interface TweetsApi {

    suspend fun getTimeline(): List<Tweet>

    suspend fun getFriends(handle: String): List<String>

    suspend fun retweet(id: Long): Boolean

    suspend fun unretweet(id: Long): Boolean

    suspend fun favorite(id: Long): Boolean

    suspend fun unfavorite(id: Long): Boolean
}

class TweetsApiImpl(
    private val statuses: StatusesService,
    private val favorites: FavoriteService,
    private val friends: FriendsService
) : TweetsApi {

    override suspend fun getTimeline(): List<Tweet> {
        var maxId: Long? = null
        val result = mutableListOf<Tweet>()

        for (times in 1..3) {
            val part = getTimeline(maxId)
            result.addAll(part)

            maxId = part.lastOrNull()?.id
            if (maxId == null) break
        }

        return result
    }

    override suspend fun getFriends(handle: String): List<String> {
        var cursor: Long? = null
        val result = mutableListOf<String>()

        while (true) {
            val part = getFriends(handle, cursor) ?: break
            result.addAll(part.ids)
            cursor = part.nextCursor ?: break
        }

        return result
    }

    override suspend fun retweet(id: Long) = try {
        statuses.retweet(id, true).execute().isSuccessful
    } catch (e: Exception) {
        false
    }

    override suspend fun unretweet(id: Long) = try {
        statuses.unretweet(id, true).execute().isSuccessful
    } catch (e: Exception) {
        false
    }

    override suspend fun favorite(id: Long) = try {
        favorites.create(id, false).execute().isSuccessful
    } catch (e: Exception) {
        false
    }

    override suspend fun unfavorite(id: Long) = try {
        favorites.destroy(id, false).execute().isSuccessful
    } catch (e: Exception) {
        false
    }

    private fun getTimeline(maxId: Long?) = try {
        statuses.homeTimeline(
            200,
            null,
            maxId,
            false,
            true,
            false,
            false
        ).execute()?.body() ?: emptyList()
    } catch (e: Exception) {
        emptyList<Tweet>()
    }

    private fun getFriends(handle: String, cursor: Long?) = try {
        friends.friends(screenName = handle, cursor = cursor).execute()?.body()
    } catch (e: Exception) {
        null
    }
}