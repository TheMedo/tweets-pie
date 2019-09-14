package com.medo.tweetspie.ui.main

import androidx.lifecycle.LiveData
import com.medo.tweetspie.data.local.PieDao
import com.medo.tweetspie.data.repository.UserRepository
import com.medo.tweetspie.util.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModel(
    userRepository: UserRepository,
    private val pieDao: PieDao,
    private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel() {

    val user: LiveData<String> = userRepository.getUser()

    override fun onCleared() {
        super.onCleared()
        GlobalScope.launch(ioDispatcher) { pieDao.deleteOldData() }
    }
}