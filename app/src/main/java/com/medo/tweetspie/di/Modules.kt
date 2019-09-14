package com.medo.tweetspie.di

import com.medo.tweetspie.repository.PieConverter
import com.medo.tweetspie.repository.PieConverterImpl
import com.medo.tweetspie.repository.TweetsRepository
import com.medo.tweetspie.repository.TweetsRepositoryImpl
import com.medo.tweetspie.repository.UserRepository
import com.medo.tweetspie.repository.UserRepositoryImpl
import com.medo.tweetspie.ui.main.MainViewModel
import com.medo.tweetspie.ui.main.PiesViewModel
import com.medo.tweetspie.ui.onboarding.OnboardingViewModel
import com.medo.tweetspie.util.di.DISPATCHER_IO
import com.medo.tweetspie.util.linkify.TwitterLinkify
import com.medo.tweetspie.util.linkify.TwitterLinkifyImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module

val mainModule = module {
    single {
        PieConverterImpl(
            get(),
            get(),
            get(),
            get(),
            get()
        ) as PieConverter
    }
    single { UserRepositoryImpl(get()) as UserRepository }
    single {
        TweetsRepositoryImpl(
            get(),
            get(),
            get(),
            get(),
            get()
        ) as TweetsRepository
    }
    single { TwitterLinkifyImpl() as TwitterLinkify }

    viewModel { OnboardingViewModel(get()) }
    viewModel { MainViewModel(get(), get(), get(StringQualifier(DISPATCHER_IO))) }
    viewModel { PiesViewModel(get(), get(), get(), get(StringQualifier(DISPATCHER_IO))) }
}