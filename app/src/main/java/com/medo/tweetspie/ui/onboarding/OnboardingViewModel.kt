package com.medo.tweetspie.ui.onboarding

import androidx.lifecycle.LiveData
import com.medo.tweetspie.base.BaseViewModel
import com.medo.tweetspie.system.UserRepository
import com.twitter.sdk.android.core.TwitterSession

class OnboardingViewModel(
        private val userRepository: UserRepository
) : BaseViewModel() {

    val user: LiveData<String> = userRepository.getUser()

    fun saveUser(twitterSession: TwitterSession) {
        userRepository.setUser(twitterSession.userName)
    }
}