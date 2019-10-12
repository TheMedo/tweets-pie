package com.medo.tweetspie.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.medo.tweetspie.R
import com.medo.tweetspie.repository.PieConverter
import com.medo.tweetspie.repository.TweetsRepository
import com.medo.tweetspie.system.Resources
import com.medo.tweetspie.util.base.BaseCoroutineViewModel
import com.medo.tweetspie.utils.ActionLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class PiesViewModel(
    private val repository: TweetsRepository,
    private val pieConverter: PieConverter,
    private val resources: Resources,
    ioDispatcher: CoroutineDispatcher
) : BaseCoroutineViewModel(ioDispatcher) {

    init {
        launch { repository.fetch() }
    }

    private val urlActionCallback: (String) -> Unit = {
        when (it[0]) {
            '#' -> urlAction.post(resources.getString(R.string.url_hashtag, it.substring(1)))
            '@' -> urlAction.post(resources.getString(R.string.url_user, it.substring(1)))
            else -> urlAction.post(it)
        }
    }
    val urlAction = ActionLiveData<String>()

    val loading: LiveData<Boolean> = repository.getLoading()
    val pies: LiveData<List<BakedPie>> = Transformations.map(repository.getPies()) {
        pieConverter.bakePies(it, urlActionCallback)
    }

    fun persistTweets() {
        launch {
            repository.persistTweets()
        }
    }

    fun retweet(id: String, retweeted: Boolean) {
        launch {
            when (retweeted) {
                true -> repository.unretweet(id.toLong())
                else -> repository.retweet(id.toLong())
            }
        }
    }

    fun favorite(id: String, favorited: Boolean) {
        launch {
            when (favorited) {
                true -> repository.unfavorite(id.toLong())
                else -> repository.favorite(id.toLong())
            }
        }
    }
}