package com.medo.tweetspie.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.medo.tweetspie.R
import com.medo.tweetspie.ui.main.MainActivity
import com.medo.tweetspie.util.base.BaseActivity
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import kotlinx.android.synthetic.main.activity_onboarding.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class OnboardingActivity : BaseActivity() {

    override val viewModel by viewModel<OnboardingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        loginButton.callback = object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>?) {
                viewModel.saveUser(result?.data)
            }

            override fun failure(exception: TwitterException?) {
                viewModel.failure.postValue(exception?.message)
            }
        }

        viewModel.user.observe(this, Observer<String?>(this::handleUser))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loginButton.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleUser(username: String?) {
        username ?: return
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
