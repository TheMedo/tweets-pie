package com.medo.tweetspie.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.commitNow
import androidx.lifecycle.Observer
import com.medo.tweetspie.R
import com.medo.tweetspie.ui.onboarding.OnboardingActivity
import com.medo.tweetspie.util.base.BaseActivity
import com.medo.tweetspie.utils.TAG_TWEETS_FRAGMENT
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity() {

    override val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.user.observe(this, Observer<String?>(this::handleUser))
    }

    private fun handleUser(username: String?) = when (username) {
        null -> startOnboarding()
        else -> showTweets()
    }

    private fun startOnboarding() {
        startActivity(Intent(this, OnboardingActivity::class.java))
        finish()
    }

    private fun showTweets() {
        val fragmentManager = supportFragmentManager
        val fragment = fragmentManager.findFragmentByTag(TAG_TWEETS_FRAGMENT) ?: PiesFragment()
        fragmentManager.commitNow {
            replace(android.R.id.content, fragment, TAG_TWEETS_FRAGMENT)
        }
    }
}