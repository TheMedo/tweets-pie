package com.medo.tweetspie.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.medo.tweetspie.base.BaseViewModel
import com.medo.tweetspie.data.local.model.Pie
import com.medo.tweetspie.data.repository.TweetsRepository

class TweetsViewModel(
    repository: TweetsRepository
) : BaseViewModel() {

    init {
        repository.fetch()
    }

    val pies: LiveData<List<Pie>> = repository.getPies()
    val loading: LiveData<Boolean> = repository.getLoading()
    val refresh: MutableLiveData<Boolean> = MutableLiveData()
}