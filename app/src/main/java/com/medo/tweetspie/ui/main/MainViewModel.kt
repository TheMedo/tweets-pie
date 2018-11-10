package com.medo.tweetspie.ui.main

import androidx.lifecycle.LiveData
import com.medo.tweetspie.base.BaseViewModel
import com.medo.tweetspie.data.repository.UserRepository

class MainViewModel(
        userRepository: UserRepository
) : BaseViewModel() {

    val user: LiveData<String> = userRepository.getUser()
}