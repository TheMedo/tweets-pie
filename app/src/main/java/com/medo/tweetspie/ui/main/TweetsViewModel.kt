package com.medo.tweetspie.ui.main

import androidx.lifecycle.LiveData
import com.medo.tweetspie.base.BaseViewModel
import com.medo.tweetspie.data.repository.TweetsRepository
import com.twitter.sdk.android.core.models.Tweet
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class TweetsViewModel(
        private val repository: TweetsRepository
) : BaseViewModel() {

    val tweets: LiveData<List<Tweet>> = repository.getTweets()

    fun fetch() = launch(UI) { repository.fetch() }
}