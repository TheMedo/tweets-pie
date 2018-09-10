package com.medo.tweetspie.di

import android.preference.PreferenceManager
import com.medo.tweetspie.data.remote.FriendsApiClient
import com.medo.tweetspie.data.remote.TweetsApi
import com.medo.tweetspie.data.remote.TweetsApiImpl
import com.medo.tweetspie.data.repository.TweetsRepository
import com.medo.tweetspie.data.repository.TweetsRepositoryImpl
import com.medo.tweetspie.system.Resources
import com.medo.tweetspie.system.ResourcesImpl
import com.medo.tweetspie.system.UserRepository
import com.medo.tweetspie.system.UserRepositoryImpl
import com.medo.tweetspie.ui.main.MainViewModel
import com.medo.tweetspie.ui.main.TweetsViewModel
import com.medo.tweetspie.ui.onboarding.OnboardingViewModel
import com.twitter.sdk.android.core.TwitterCore
import org.koin.dsl.module.applicationContext

fun getAppModules() = listOf(mainModule)

val mainModule = applicationContext {

    //    bean { Room.databaseBuilder(androidApplication(), WeatherDatabase::class.java, "weather_db").build() }
//    bean { get<WeatherDatabase>().weatherDao() }
//    bean { WeatherRepositoryImpl(get(), get()) as WeatherRepository }

    bean { ResourcesImpl(get()) as Resources }
    bean { UserRepositoryImpl(get()) as UserRepository }
    bean { TweetsApiImpl(get(), get()) as TweetsApi }
    bean { TweetsRepositoryImpl(get(), get()) as TweetsRepository }

    bean { PreferenceManager.getDefaultSharedPreferences(get()) }
    bean { TwitterCore.getInstance().sessionManager.activeSession }
    bean { TwitterCore.getInstance().apiClient.statusesService }
    bean { FriendsApiClient(get()).friendsService }

    viewModelX { OnboardingViewModel(get()) }
    viewModelX { MainViewModel(get()) }
    viewModelX { TweetsViewModel(get()) }
}