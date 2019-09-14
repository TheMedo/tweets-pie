package com.medo.tweetspie.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.medo.tweetspie.R
import com.medo.tweetspie.data.repository.TweetsRepository
import com.medo.tweetspie.system.Resources
import com.medo.tweetspie.util.base.BaseCoroutineViewModel
import com.medo.tweetspie.utils.ActionLiveData
import com.medo.tweetspie.utils.PieConverter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class PiesViewModel(
    repository: TweetsRepository,
    private val pieConverter: PieConverter,
    private val resources: Resources,
    ioDispatcher: CoroutineDispatcher
) : BaseCoroutineViewModel(ioDispatcher) {

    init {
        launch { repository.fetch() }
    }

    private val urlActionCallback: (String) -> Unit = {
        when (it.substring(1)) {
            "#" -> urlAction.post(resources.getString(R.string.url_hashtag, it))
            "@" -> urlAction.post(resources.getString(R.string.url_user, it))
            else -> urlAction.post(it)
        }
    }
    val urlAction = ActionLiveData<String>()

    val loading: LiveData<Boolean> = repository.getLoading()
    val refresh: MutableLiveData<Boolean> = MutableLiveData()
    val pies: LiveData<List<BakedPie>> = Transformations.map(repository.getPies()) {
        pieConverter.bakePies(it, urlActionCallback)
    }
}