package com.medo.tweetspie.ui.onboarding

import androidx.lifecycle.LiveData
import com.medo.tweetspie.repository.UserRepository
import com.medo.tweetspie.util.base.BaseViewModel
import com.twitter.sdk.android.core.TwitterSession

class OnboardingViewModel(
    private val userRepository: UserRepository
) : BaseViewModel() {

    val user: LiveData<String> = userRepository.getUser()

    fun saveUser(twitterSession: TwitterSession?) {
        when (twitterSession) {
            null -> failure.postValue("Cannot login")
            else -> userRepository.setUser(twitterSession.userName)
        }
    }
}