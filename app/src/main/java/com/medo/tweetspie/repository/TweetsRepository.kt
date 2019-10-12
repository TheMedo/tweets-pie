package com.medo.tweetspie.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import com.medo.tweetspie.remote.api.TweetsApi
import com.medo.tweetspie.storage.PieDao
import com.medo.tweetspie.storage.model.PieFriend
import com.medo.tweetspie.storage.model.RawPie
import com.medo.tweetspie.system.Clock
import com.medo.tweetspie.util.events.SingleLiveEvent
import com.twitter.sdk.android.core.models.Tweet

const val KEY_TWEETS_TIMESTAMP = "keyTweetsTimestamp"
const val TIMELINE_REFRESH_INTERVAL = 1 * 60 * 1000
const val KEY_FRIENDS_TIMESTAMP = "keyFriendsTimestamp"
const val FRIENDS_REFRESH_INTERVAL = 24 * 60 * 60 * 1000

interface TweetsRepository {

    suspend fun fetch()

    fun getPies(): LiveData<List<RawPie>>

    fun getLoading(): LiveData<Boolean>

    suspend fun persistTweets()

    suspend fun retweet(id: Long)

    suspend fun unretweet(id: Long)

    suspend fun favorite(id: Long)

    suspend fun unfavorite(id: Long)
}

class TweetsRepositoryImpl(
    private val prefs: SharedPreferences,
    private val clock: Clock,
    private val converter: PieConverter,
    private val tweetsApi: TweetsApi,
    private val pieDao: PieDao
) : TweetsRepository {

    private val data = pieDao.getPies()
    private val loading = SingleLiveEvent<Boolean>()

    private var remoteTweets: List<Tweet>? = null

    override fun getPies() = data

    override fun getLoading() = loading

    override suspend fun fetch() {
        val lastFriendsUpdate = prefs.getLong(KEY_FRIENDS_TIMESTAMP, 0)
        if (clock.currentTime - lastFriendsUpdate > FRIENDS_REFRESH_INTERVAL) {
            val handle = prefs.getString(KEY_HANDLE, "") ?: ""
            val friends = tweetsApi.getFriends(handle)
            persistFriends(friends)
        }

        val lastUpdate = prefs.getLong(KEY_TWEETS_TIMESTAMP, 0)
        if (clock.currentTime - lastUpdate > TIMELINE_REFRESH_INTERVAL) {
            loading.postValue(true)
            remoteTweets = tweetsApi.getTimeline()
            loading.postValue(false)
        }
    }

    override suspend fun persistTweets() {
        remoteTweets?.let {
            persistTweets(it)
            remoteTweets = null
        }

    }

    override suspend fun retweet(id: Long) {
        pieDao.retweet(id)
        if (!tweetsApi.retweet(id)) {
            pieDao.unretweet(id)
        }
    }

    override suspend fun unretweet(id: Long) {
        pieDao.unretweet(id)
        if (!tweetsApi.unretweet(id)) {
            pieDao.retweet(id)
        }
    }

    override suspend fun favorite(id: Long) {
        pieDao.favorite(id)
        if (!tweetsApi.favorite(id)) {
            pieDao.unfavorite(id)
        }
    }

    override suspend fun unfavorite(id: Long) {
        pieDao.unfavorite(id)
        if (!tweetsApi.unfavorite(id)) {
            pieDao.favorite(id)
        }
    }

    private fun persistFriends(friends: List<String>) = friends.map { PieFriend(it) }

    private fun persistTweets(remoteTweets: List<Tweet>) {
        if (remoteTweets.isEmpty()) return
        val convertedTweets = converter.createPies(remoteTweets)
        pieDao.insert(convertedTweets.map { it.pie })
        pieDao.insertMedia(convertedTweets.flatMap { it.media })
        prefs.edit {
            putLong(
                KEY_TWEETS_TIMESTAMP,
                clock.currentTime
            )
        }
    }
}