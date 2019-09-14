package com.medo.tweetspie.util.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    var failure: MutableLiveData<String> = MutableLiveData()

    protected fun handleFailure(failure: String) {
        this.failure.value = failure
    }
}