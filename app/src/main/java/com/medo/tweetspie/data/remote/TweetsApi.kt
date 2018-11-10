package com.medo.tweetspie.data.remote

import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.services.StatusesService

interface TweetsApi {

    fun getTimeline(): List<Tweet>

    fun getFriends(handle: String): List<String>
}

class TweetsApiImpl(
    private val statuses: StatusesService,
    private val friends: FriendsService
) : TweetsApi {

    override fun getTimeline(): List<Tweet> {
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

    override fun getFriends(handle: String): List<String> {
        var cursor: Long? = null
        val result = mutableListOf<String>()

        while (true) {
            val part = getFriends(handle, cursor) ?: break
            result.addAll(part.ids)
            cursor = part.nextCursor ?: break
        }

        return result
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