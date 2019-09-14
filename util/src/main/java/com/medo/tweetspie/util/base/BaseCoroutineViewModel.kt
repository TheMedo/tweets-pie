package com.medo.tweetspie.util.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseCoroutineViewModel(
    private val dispatcher: CoroutineDispatcher
) : BaseViewModel(), CoroutineScope {

    private val job by lazy { Job() }

    override val coroutineContext: CoroutineContext
        get() = job + dispatcher

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}