package com.medo.tweetspie.di

import android.preference.PreferenceManager
import androidx.room.Room
import com.medo.tweetspie.data.local.PieDatabase
import com.medo.tweetspie.data.remote.FriendsApiClient
import com.medo.tweetspie.data.remote.TweetsApi
import com.medo.tweetspie.data.remote.TweetsApiImpl
import com.medo.tweetspie.data.repository.TweetsRepository
import com.medo.tweetspie.data.repository.TweetsRepositoryImpl
import com.medo.tweetspie.data.repository.UserRepository
import com.medo.tweetspie.data.repository.UserRepositoryImpl
import com.medo.tweetspie.system.Clock
import com.medo.tweetspie.system.ClockImpl
import com.medo.tweetspie.system.Resources
import com.medo.tweetspie.system.ResourcesImpl
import com.medo.tweetspie.ui.main.MainViewModel
import com.medo.tweetspie.ui.main.TweetsViewModel
import com.medo.tweetspie.ui.onboarding.OnboardingViewModel
import com.medo.tweetspie.utils.Formatter
import com.medo.tweetspie.utils.FormatterImpl
import com.twitter.sdk.android.core.TwitterCore
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

fun getAppModules() = listOf(mainModule)

val mainModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(), PieDatabase::class.java, "pie_db"
        ).build()
    }
    single { get<PieDatabase>().pieDao() }
    single { FormatterImpl(get()) as Formatter }
    single { ResourcesImpl(get()) as Resources }
    single { UserRepositoryImpl(get()) as UserRepository }
    single { TweetsApiImpl(get(), get()) as TweetsApi }
    single {
        TweetsRepositoryImpl(
            get("IO"),
            get(),
            get(),
            get(),
            get(),
            get()
        ) as TweetsRepository
    }
    single { ClockImpl() as Clock }
    single("IO") { Dispatchers.IO }
    single("MAIN") { Dispatchers.Main }

    single { PreferenceManager.getDefaultSharedPreferences(get()) }
    single { TwitterCore.getInstance().sessionManager.activeSession }
    single { TwitterCore.getInstance().apiClient.statusesService }
    single { FriendsApiClient(get()).friendsService }

    viewModel { OnboardingViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { TweetsViewModel(get()) }
}