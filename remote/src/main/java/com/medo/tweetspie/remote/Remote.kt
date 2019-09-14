package com.medo.tweetspie.remote

import android.app.Application
import android.util.Log
import com.twitter.sdk.android.core.DefaultLogger
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig

class Remote {

    companion object {
        fun init(app: Application) {
            // init twitter
            val config = TwitterConfig.Builder(app)
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
}