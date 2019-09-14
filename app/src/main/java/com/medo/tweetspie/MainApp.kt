package com.medo.tweetspie

import android.app.Application
import android.util.Log
import com.medo.tweetspie.di.mainModule
import com.medo.tweetspie.storage.di.storageModule
import com.medo.tweetspie.system.di.systemModule
import com.medo.tweetspie.util.di.utilModule
import com.twitter.sdk.android.core.DefaultLogger
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // init DI
        startKoin {
            androidContext(this@MainApp)
            modules(listOf(mainModule, systemModule, utilModule, storageModule))
        }
        // init twitter
        val config = TwitterConfig.Builder(this)
            .logger(DefaultLogger(Log.DEBUG))
            .twitterAuthConfig(
                TwitterAuthConfig(
                    BuildConfig.TWITTER_KEY,
                    BuildConfig.TWITTER_SECRET
                )
            )
            .debug(true)
            .build()
        Twitter.initialize(config)
    }
}
