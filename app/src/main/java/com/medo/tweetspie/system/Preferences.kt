package com.medo.tweetspie.system

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.annotation.StringDef

const val USERNAME = "username"
const val RETWEETS = "retweets"
const val REPLIES = "replies"
const val LAST_UPDATE_TIMESTAMP = "lastUpdateTimestamp"
const val MAX_TWEETS = "maxTweets"

@Retention(AnnotationRetention.SOURCE)
@StringDef(USERNAME, RETWEETS, REPLIES, LAST_UPDATE_TIMESTAMP, MAX_TWEETS)
annotation class PreferenceKey

interface Preferences {

    fun initWithDefaultValues()

    fun getString(@PreferenceKey key: String): String

    fun getLong(@PreferenceKey key: String): Long

    fun getBoolean(@PreferenceKey key: String): Boolean

    operator fun set(@PreferenceKey key: String, value: String)

    operator fun set(@PreferenceKey key: String, value: Long)

    operator fun set(@PreferenceKey key: String, value: Boolean)

    fun has(@PreferenceKey key: String): Boolean

    fun remove(@PreferenceKey key: String)
}

class PreferencesImpl(context: Context) : Preferences {

    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)

    override fun initWithDefaultValues() {
        set(RETWEETS, true)
        set(REPLIES, true)
        set(MAX_TWEETS, 30)
    }

    override fun getString(@PreferenceKey key: String): String = prefs.getString(key, "")

    override fun getLong(@PreferenceKey key: String) = prefs.getLong(key, 0)

    override fun getBoolean(@PreferenceKey key: String) = prefs.getBoolean(key, false)

    override fun set(@PreferenceKey key: String, value: String) = prefs.edit().putString(key, value).apply()

    override fun set(@PreferenceKey key: String, value: Long) = prefs.edit().putLong(key, value).apply()

    override fun set(@PreferenceKey key: String, value: Boolean) = prefs.edit().putBoolean(key, value).apply()

    override fun has(@PreferenceKey key: String) = prefs.contains(key)

    override fun remove(@PreferenceKey key: String) = prefs.edit().remove(key).apply()
}