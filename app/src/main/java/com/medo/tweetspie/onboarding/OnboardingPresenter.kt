package com.medo.tweetspie.onboarding

import com.medo.tweetspie.R
import com.medo.tweetspie.base.AbsBasePresenter
import com.medo.tweetspie.system.Preferences
import com.medo.tweetspie.system.Resources
import com.medo.tweetspie.system.USERNAME
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import timber.log.Timber

class OnboardingPresenter(
        private val preferences: Preferences,
        private val resources: Resources
) : AbsBasePresenter<OnboardingContract.View>(), OnboardingContract.Presenter {

    override fun onAttach(view: OnboardingContract.View) {
        super.onAttach(view)
        view.setupTwitterButton(object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>) {
                onLoginSuccess(result.data)
            }

            override fun failure(exception: TwitterException) {
                onLoginFailure(exception)
            }
        })
    }

    override fun onLoginSuccess(twitterSession: TwitterSession) {
        preferences.initWithDefaultValues()
        preferences[USERNAME] = twitterSession.userName
        view?.exit()
    }

    override fun onLoginFailure(e: Exception) {
        Timber.e(e, "Cannot login with Twitter")
        view?.exitWithError(resources.getString(R.string.error_login_failure))
    }
}