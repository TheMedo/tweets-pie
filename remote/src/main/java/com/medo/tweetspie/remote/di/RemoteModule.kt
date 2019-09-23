package com.medo.tweetspie.remote.di

import com.medo.tweetspie.remote.api.FriendsApiClient
import com.medo.tweetspie.remote.api.TweetsApi
import com.medo.tweetspie.remote.api.TweetsApiImpl
import com.twitter.sdk.android.core.TwitterCore
import org.koin.dsl.module


val remoteModule = module {
    single { TwitterCore.getInstance().sessionManager.activeSession }
    single { TwitterCore.getInstance().apiClient.statusesService }
    single { TwitterCore.getInstance().apiClient.favoriteService }
    single {
        TweetsApiImpl(
            get(),
            get(),
            get()
        ) as TweetsApi
    }
    single { FriendsApiClient(get()).friendsService }
}