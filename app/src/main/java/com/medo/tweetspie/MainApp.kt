package com.medo.tweetspie

import android.app.Application
import android.util.Log
import com.medo.tweetspie.di.getAppModules
import com.twitter.sdk.android.core.DefaultLogger
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig
import org.koin.android.ext.android.startKoin


class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // init DI
        startKoin(this, getAppModules())
        // init twitter
        val config = TwitterConfig.Builder(this)
                .logger(DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(TwitterAuthConfig(BuildConfig.TWITTER_KEY, BuildConfig.TWITTER_SECRET))
                .debug(true)
                .build()
        Twitter.initialize(config)
    }
}
