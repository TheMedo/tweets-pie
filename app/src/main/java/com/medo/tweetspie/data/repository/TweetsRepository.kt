package com.medo.tweetspie.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.medo.tweetspie.data.remote.TweetsApi
import com.twitter.sdk.android.core.models.Tweet


const val KEY_LAST_UPDATE_TIMESTAMP = "keyLastUpdateTimestamp"
const val TIMELINE_REFRESH_INTERVAL = 1 * 5 * 1000

interface TweetsRepository {

    fun getTweets(): LiveData<List<Tweet>>

    suspend fun fetch()
}

class TweetsRepositoryImpl(
        private val prefs: SharedPreferences,
        private val tweetsApi: TweetsApi
) : TweetsRepository {

    private val data = MutableLiveData<List<Tweet>>()

    override fun getTweets() = data

    override suspend fun fetch() {
        val lastUpdate = prefs.getLong(KEY_LAST_UPDATE_TIMESTAMP, 0)
        if (System.currentTimeMillis() - lastUpdate < TIMELINE_REFRESH_INTERVAL) {
            return
        }

        val tweets = tweetsApi.getTimeline()
        data.postValue(tweets)

        prefs.edit { putLong(KEY_LAST_UPDATE_TIMESTAMP, System.currentTimeMillis()) }
    }

}