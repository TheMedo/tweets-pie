package com.medo.tweetspie.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.medo.tweetspie.data.local.PieDao
import com.medo.tweetspie.data.local.model.Pie
import com.medo.tweetspie.data.local.model.PieFriend
import com.medo.tweetspie.data.remote.TweetsApi
import com.medo.tweetspie.system.Clock
import com.twitter.sdk.android.core.models.Tweet

const val KEY_TWEETS_TIMESTAMP = "keyTweetsTimestamp"
const val TIMELINE_REFRESH_INTERVAL = 1 * 30 * 1000
const val KEY_FRIENDS_TIMESTAMP = "keyFriendsTimestamp"
const val FRIENDS_REFRESH_INTERVAL = 24 * 60 * 60 * 1000

interface TweetsRepository {

    fun fetch()

    fun getPies(): LiveData<List<Pie>>

    fun getLoading(): LiveData<Boolean>
}

class TweetsRepositoryImpl(
    private val prefs: SharedPreferences,
    private val clock: Clock,
    private val converter: TweetsConverter,
    private val tweetsApi: TweetsApi,
    private val pieDao: PieDao
) : TweetsRepository {

    private val data = pieDao.getPies()
    private val loading = MutableLiveData<Boolean>()

    override fun getPies() = data

    override fun getLoading() = loading

    override fun fetch() {
        loading.postValue(true)
        val lastFriendsUpdate = prefs.getLong(KEY_FRIENDS_TIMESTAMP, 0)
        if (clock.getCurrentTime() - lastFriendsUpdate > FRIENDS_REFRESH_INTERVAL) {
            val handle = prefs.getString(KEY_HANDLE, "") ?: ""
            val friends = tweetsApi.getFriends(handle)
            persistFriends(friends)
        }

        val lastUpdate = prefs.getLong(KEY_TWEETS_TIMESTAMP, 0)
        if (clock.getCurrentTime() - lastUpdate > TIMELINE_REFRESH_INTERVAL) {
            val remoteTweets = tweetsApi.getTimeline()
            persistTweets(remoteTweets)
        }
        loading.postValue(false)
    }

    private fun persistFriends(friends: List<String>) = friends.map { PieFriend(it) }

    private fun persistTweets(remoteTweets: List<Tweet>) {
        if (remoteTweets.isEmpty()) return
        val convertedTweets = converter.convertTweets(remoteTweets)
        pieDao.insert(convertedTweets)
        prefs.edit {
            putLong(
                KEY_TWEETS_TIMESTAMP,
                clock.getCurrentTime()
            )
        }
    }
}