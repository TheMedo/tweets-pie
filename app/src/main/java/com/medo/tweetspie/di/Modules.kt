package com.medo.tweetspie.di

import android.preference.PreferenceManager
import com.medo.tweetspie.system.Resources
import com.medo.tweetspie.system.ResourcesImpl
import com.medo.tweetspie.system.UserRepository
import com.medo.tweetspie.system.UserRepositoryImpl
import com.medo.tweetspie.ui.main.MainViewModel
import com.medo.tweetspie.ui.onboarding.OnboardingViewModel
import org.koin.dsl.module.applicationContext

fun getAppModules() = listOf(mainModule)

val mainModule = applicationContext {

    //    bean { Room.databaseBuilder(androidApplication(), WeatherDatabase::class.java, "weather_db").build() }
//    bean { get<WeatherDatabase>().weatherDao() }
//    bean { WeatherRepositoryImpl(get(), get()) as WeatherRepository }

    bean { ResourcesImpl(get()) as Resources }
    bean { UserRepositoryImpl(get()) as UserRepository }

    bean { PreferenceManager.getDefaultSharedPreferences(get()) }

    viewModelX { OnboardingViewModel(get()) }
    viewModelX { MainViewModel(get()) }
}