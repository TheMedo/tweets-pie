package com.medo.tweetspie.onboarding

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.medo.tweetspie.R
import com.medo.tweetspie.base.BaseActivity
import com.medo.tweetspie.di.BindContext
import com.medo.tweetspie.extensions.toast
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.TwitterSession
import kotlinx.android.synthetic.main.activity_onboarding.*
import org.koin.android.ext.android.inject

class OnboardingActivity : BaseActivity(), OnboardingMvp.View {

    override val contextName: String = BindContext.Onboarding
    override val presenter by inject<OnboardingMvp.Presenter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        presenter.onAttach(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        // Pass the activity result to the login button.
        super.onActivityResult(requestCode, resultCode, data)
        loginButton.onActivityResult(requestCode, resultCode, data)
    }

    override fun setupTwitterButton(callback: Callback<TwitterSession>) {
        loginButton.callback = callback
    }

    override fun exitWithError(errorMessage: String) {
        toast(errorMessage)
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun exit() {
        setResult(Activity.RESULT_OK)
        finish()
    }
}
