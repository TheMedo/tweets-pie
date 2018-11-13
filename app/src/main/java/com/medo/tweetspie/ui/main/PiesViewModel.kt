package com.medo.tweetspie.ui.main

import android.text.Spanned
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.medo.tweetspie.R
import com.medo.tweetspie.base.BaseViewModel
import com.medo.tweetspie.data.local.model.Pie
import com.medo.tweetspie.data.repository.TweetsRepository
import com.medo.tweetspie.system.Resources
import com.medo.tweetspie.utils.ActionLiveData
import com.medo.tweetspie.utils.Formatter
import com.medo.tweetspie.utils.linkify.TwitterLinkify
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class PiesViewModel(
    repository: TweetsRepository,
    private val formatter: Formatter,
    private val resources: Resources,
    private val linkify: TwitterLinkify,
    private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = job + ioDispatcher

    private val job = Job()

    init {
        launch { repository.fetch() }
    }

    val loading: LiveData<Boolean> = repository.getLoading()
    val refresh: MutableLiveData<Boolean> = MutableLiveData()
    val pies: LiveData<List<BakedPie>> = Transformations.map(repository.getPies()) { bakePies(it) }

    val urlAction = ActionLiveData<String>()

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    private fun bakePies(pies: List<Pie>) = pies.map {
        BakedPie(
            it.user.avatarUrl,
            it.user.name,
            "@${it.user.handle}",
            it.user.protected,
            it.user.verified,
            formatTimestamp(it.createdAt),
            formatInfo(it),
            formatText(it.text),
            it.retweeted,
            formatNumber(it.retweetCount),
            it.favorited,
            formatNumber(it.favoriteCount),
            formatNumber(it.score),
            formatUserUrl(it.user.handle),
            formatTweetUrl(it.user.handle, it.pieId)
        )
    }

    private fun formatTimestamp(createdAt: String) =
        formatter.toRelativeDate(formatter.utcToDate(createdAt))

    private fun formatInfo(pie: Pie): String {
        val builder = StringBuilder()
        val retweet = pie.retweetedBy
        if (retweet != null) builder.appendln(
            resources.getString(
                R.string.hint_retweet,
                "@$retweet"
            )
        )
        val reply = pie.inReplyTo
        if (reply != null) builder.appendln(
            resources.getString(
                R.string.hint_reply, reply
            )
        )
        val quote = pie.quoted
        if (quote != null) builder.appendln(
            resources.getString(
                R.string.hint_quote, quote
            )
        )
        return builder.trim().toString()
    }

    private fun formatText(text: String): Spanned {
        val color = resources.getColor(R.color.colorPrimaryDark)
        val action: (String) -> Unit = {
            when {
                it.startsWith("#") -> urlAction.post(formatHashtagUrl(it.substring(1)))
                it.startsWith("@") -> urlAction.post(formatUserUrl(it.substring(1)))
                else -> urlAction.post(it)
            }
        }
        return linkify.linkifyText(text, color, action)
    }

    private fun formatNumber(count: Int) = formatter.formatNumber(count.toLong())

    private fun formatTweetUrl(handle: String, id: String) =
        resources.getString(R.string.url_tweet, handle, id)

    private fun formatUserUrl(handle: String) =
        resources.getString(R.string.url_user, handle)

    private fun formatHashtagUrl(hashtag: String) =
        resources.getString(R.string.url_hashtag, hashtag)
}