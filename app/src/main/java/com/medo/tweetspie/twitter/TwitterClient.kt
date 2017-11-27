package com.medo.tweetspie.twitter

import android.support.annotation.IntRange
import com.twitter.sdk.android.core.SessionManager
import com.twitter.sdk.android.core.TwitterApiClient
import com.twitter.sdk.android.core.TwitterSession
import com.twitter.sdk.android.core.models.Tweet
import retrofit2.Response
import timber.log.Timber

private const val TIMELINE_MAX_QUERIES = 1

interface TwitterClient {

    fun checkSession(): Boolean

    fun getTimeline(): List<Tweet>

    fun getFriendsIds(username: String): List<Long>

    fun retweetStatus(tweetId: String, retweet: Boolean): Boolean

    fun favoriteStatus(tweetId: String, favorite: Boolean): Boolean
}

class TwitterClientImpl(
        private val sessionManager: SessionManager<TwitterSession>,
        private val apiClient: TwitterApiClient,
        private val friendsClient: FriendsApiClient
) : TwitterClient {

    override fun checkSession(): Boolean = sessionManager.activeSession != null

    override fun getTimeline(): List<Tweet> = getTimeline(1, null)

    override fun getFriendsIds(username: String): List<Long> = getFriendsIds(username, -1)

    override fun retweetStatus(tweetId: String, retweet: Boolean): Boolean {
        return try {
            if (retweet) apiClient.statusesService.retweet(tweetId.toLong(), false).execute().isSuccessful
            else apiClient.statusesService.unretweet(tweetId.toLong(), false).execute().isSuccessful
        } catch (exception: Exception) {
            Timber.e(exception)
            false
        }
    }

    override fun favoriteStatus(tweetId: String, favorite: Boolean): Boolean {
        return try {
            if (favorite) apiClient.favoriteService.create(tweetId.toLong(), false).execute().isSuccessful
            else apiClient.favoriteService.destroy(tweetId.toLong(), false).execute().isSuccessful
        } catch (exception: Exception) {
            Timber.e(exception)
            false
        }
    }

    private fun getTimeline(@IntRange(from = 1, to = 3) part: Int, maxId: Long?): List<Tweet> {
        var response: Response<List<Tweet>>? = null
        try {
            response = apiClient.statusesService
                    .homeTimeline(200, null, maxId, false, true, false, false)
                    .execute()
        } catch (exception: Exception) {
            Timber.e(exception)
        }

        val tweets = response?.body()
        tweets ?: return emptyList()

        if (part < TIMELINE_MAX_QUERIES) getTimeline(part + 1, tweets[tweets.size - 1].id)

        return tweets
    }

    private fun getFriendsIds(username: String, cursor: Long): List<Long> {
        var response: Response<FriendsIds>? = null
        try {
            response = friendsClient.friendsService
                    .friends(null, username, cursor, false, 5000L)
                    .execute()
        } catch (exception: Exception) {
            Timber.e(exception)
        }

        val friendIds = response?.body()
        friendIds ?: return emptyList()

        val beforeId = friendIds.previousCursor
        if (beforeId > 0) getFriendsIds(username, beforeId)

        return friendIds.ids
    }
}