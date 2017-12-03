package com.medo.tweetspie.di

import com.medo.tweetspie.di.BindContext.Main
import com.medo.tweetspie.di.BindContext.Onboarding
import com.medo.tweetspie.di.BindContext.Service
import com.medo.tweetspie.main.MainMvp
import com.medo.tweetspie.main.MainPresenter
import com.medo.tweetspie.onboarding.OnboardingMvp
import com.medo.tweetspie.onboarding.OnboardingPresenter
import com.medo.tweetspie.service.ServiceMvp
import com.medo.tweetspie.service.ServicePresenter
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
        MainModule(),
        ServiceModule()
)

class OnboardingModule : AndroidModule() {
    override fun context() = applicationContext {
        context(name = Onboarding) {
            provide { OnboardingPresenter(get(), get()) } bind (OnboardingMvp.Presenter::class)
        }
        provide { PreferencesImpl(get()) } bind (Preferences::class)
        provide { ResourcesImpl(get()) } bind (Resources::class)
        provide { TwitterClientImpl(getTwitterSession(), getTwitterClient(), getFriendsClient()) } bind (TwitterClient::class)
    }
}

class MainModule : AndroidModule() {
    override fun context() = applicationContext {
        context(name = Main) {
            provide { MainPresenter(get(), get()) } bind (MainMvp.Presenter::class)
        }
    }
}

class ServiceModule : AndroidModule() {
    override fun context() = applicationContext {
        context(name = Service) {
            provide { ServicePresenter(get(), get()) } bind (ServiceMvp.Presenter::class)
        }
    }
}

fun getTwitterSession(): SessionManager<TwitterSession> = TwitterCore.getInstance().sessionManager

fun getTwitterClient(): TwitterApiClient = TwitterCore.getInstance().apiClient

fun getFriendsClient(): FriendsApiClient = FriendsApiClient(getTwitterSession().activeSession)

object BindContext {
    val Onboarding = "Onboarding"
    val Main = "Main"
    val Service = "Service"
}