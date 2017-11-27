package com.medo.tweetspie.onboarding

import com.medo.tweetspie.base.BasePresenter
import com.medo.tweetspie.base.BaseView
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.TwitterSession


interface OnboardingContract {

    interface View : BaseView {

        fun setupTwitterButton(callback: Callback<TwitterSession>)

        fun exitWithError(errorMessage: String)
    }


    interface Presenter : BasePresenter<View> {

        fun onLoginSuccess(twitterSession: TwitterSession)

        fun onLoginFailure(e: Exception)
    }
}