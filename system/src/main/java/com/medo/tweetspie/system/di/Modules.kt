package com.medo.tweetspie.system.di

import androidx.preference.PreferenceManager
import com.medo.tweetspie.system.Clock
import com.medo.tweetspie.system.ClockImpl
import com.medo.tweetspie.system.Formatter
import com.medo.tweetspie.system.FormatterImpl
import com.medo.tweetspie.system.Resources
import com.medo.tweetspie.system.ResourcesImpl
import org.koin.dsl.module


val systemModule = module {
    single { FormatterImpl(get()) as Formatter }
    single { ResourcesImpl(get()) as Resources }
    single { ClockImpl() as Clock }
    single { PreferenceManager.getDefaultSharedPreferences(get()) }
}