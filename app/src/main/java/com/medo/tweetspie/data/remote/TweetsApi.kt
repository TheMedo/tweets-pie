package com.medo.tweetspie.data.remote

import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.services.StatusesService
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.withContext

interface TweetsApi {

    suspend fun getTimeline(): List<Tweet>
}

class TweetsApiImpl(
        private val statuses: StatusesService,
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

    private suspend fun getTimeline(maxId: Long?) = withContext(CommonPool) {
        try {
            statuses.homeTimeline(200, null, maxId, false, true, false, false).execute()
        } catch (e: Exception) {
            null
        }
    }?.body() ?: emptyList()
}