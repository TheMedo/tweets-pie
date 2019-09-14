package com.medo.tweetspie.di

import androidx.preference.PreferenceManager
import androidx.room.Room
import com.medo.tweetspie.data.local.PieDatabase
import com.medo.tweetspie.data.remote.FriendsApiClient
import com.medo.tweetspie.data.remote.TweetsApi
import com.medo.tweetspie.data.remote.TweetsApiImpl
import com.medo.tweetspie.data.repository.TweetsConverter
import com.medo.tweetspie.data.repository.TweetsConverterImpl
import com.medo.tweetspie.data.repository.TweetsRepository
import com.medo.tweetspie.data.repository.TweetsRepositoryImpl
import com.medo.tweetspie.data.repository.UserRepository
import com.medo.tweetspie.data.repository.UserRepositoryImpl
import com.medo.tweetspie.system.Clock
import com.medo.tweetspie.system.ClockImpl
import com.medo.tweetspie.system.Formatter
import com.medo.tweetspie.system.FormatterImpl
import com.medo.tweetspie.system.Resources
import com.medo.tweetspie.system.ResourcesImpl
import com.medo.tweetspie.ui.main.MainViewModel
import com.medo.tweetspie.ui.main.PiesViewModel
import com.medo.tweetspie.ui.onboarding.OnboardingViewModel
import com.medo.tweetspie.util.linkify.TwitterLinkify
import com.medo.tweetspie.util.linkify.TwitterLinkifyImpl
import com.medo.tweetspie.utils.PieBaker
import com.medo.tweetspie.utils.PieBakerImpl
import com.twitter.sdk.android.core.TwitterCore
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module

const val DISPATCHER_MAIN = "MAIN"
const val DISPATCHER_IO = "IO"

val mainModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(), PieDatabase::class.java, "pie_db"
        ).build()
    }
    single { get<PieDatabase>().pieDao() }
    single { FormatterImpl(get()) as Formatter }
    single { ResourcesImpl(get()) as Resources }
    single { TweetsConverterImpl(get(), get(), get()) as TweetsConverter }
    single { UserRepositoryImpl(get()) as UserRepository }
    single { TweetsApiImpl(get(), get()) as TweetsApi }
    single {
        TweetsRepositoryImpl(
            get(),
            get(),
            get(),
            get(),
            get()
        ) as TweetsRepository
    }
    single { ClockImpl() as Clock }
    single { TwitterLinkifyImpl() as TwitterLinkify }
    single { PieBakerImpl(get(), get(), get()) as PieBaker }
    single(StringQualifier(DISPATCHER_IO)) { Dispatchers.IO }
    single(StringQualifier(DISPATCHER_MAIN)) { Dispatchers.Main }

    single { PreferenceManager.getDefaultSharedPreferences(get()) }
    single { TwitterCore.getInstance().sessionManager.activeSession }
    single { TwitterCore.getInstance().apiClient.statusesService }
    single { FriendsApiClient(get()).friendsService }

    viewModel { OnboardingViewModel(get()) }
    viewModel { MainViewModel(get(), get(), get(StringQualifier(DISPATCHER_IO))) }
    viewModel { PiesViewModel(get(), get(), get(), get(StringQualifier(DISPATCHER_IO))) }
}