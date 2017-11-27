package com.medo.tweetspie.di

import com.medo.tweetspie.di.Context.Main
import com.medo.tweetspie.di.Context.Onboarding
import com.medo.tweetspie.main.MainContract
import com.medo.tweetspie.main.MainPresenter
import com.medo.tweetspie.onboarding.OnboardingContract
import com.medo.tweetspie.onboarding.OnboardingPresenter
import com.medo.tweetspie.system.Preferences
import com.medo.tweetspie.system.PreferencesImpl
import com.medo.tweetspie.system.Resources
import com.medo.tweetspie.system.ResourcesImpl
import com.medo.tweetspie.twitter.FriendsApiClient
import com.medo.tweetspie.twitter.TwitterClient
import com.medo.tweetspie.twitter.TwitterClientImpl
import com.twitter.sdk.android.core.SessionManager
import com.twitter.sdk.android.core.TwitterApiClient
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterSession
import org.koin.android.module.AndroidModule

fun tweetsPieAppModules() = listOf(
        OnboardingModule(),
        MainModule()
)

class OnboardingModule : AndroidModule() {
    override fun context() = applicationContext {
        context(name = Onboarding) {
            provide { OnboardingPresenter(get(), get()) } bind (OnboardingContract.Presenter::class)
        }
        provide { PreferencesImpl(get()) } bind (Preferences::class)
        provide { ResourcesImpl(get()) } bind (Resources::class)
    }
}

class MainModule : AndroidModule() {
    override fun context() = applicationContext {
        context(name = Main) {
            provide { MainPresenter(get(), get()) } bind (MainContract.Presenter::class)
            provide { TwitterClientImpl(get(), get(), get()) } bind (TwitterClient::class)
        }
    }
}

fun getTwitterSession(): SessionManager<TwitterSession> = TwitterCore.getInstance().sessionManager

fun getTwitterClient(): TwitterApiClient = TwitterCore.getInstance().apiClient

fun getFriendsClient(): FriendsApiClient = FriendsApiClient(getTwitterSession().activeSession)

object Context {
    val Onboarding = "Onboarding"
    val Main = "Main"
}