package com.medo.tweetspie.ui.main

import android.text.Spanned
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.medo.tweetspie.R
import com.medo.tweetspie.data.local.model.Pie
import com.medo.tweetspie.data.local.model.RawPie
import com.medo.tweetspie.data.repository.TweetsRepository
import com.medo.tweetspie.system.Formatter
import com.medo.tweetspie.system.Resources
import com.medo.tweetspie.util.base.BaseCoroutineViewModel
import com.medo.tweetspie.util.linkify.TwitterLinkify
import com.medo.tweetspie.utils.ActionLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class PiesViewModel(
    repository: TweetsRepository,
    private val formatter: Formatter,
    private val resources: Resources,
    private val linkify: TwitterLinkify,
    ioDispatcher: CoroutineDispatcher
) : BaseCoroutineViewModel(ioDispatcher) {

    init {
        launch { repository.fetch() }
    }

    val loading: LiveData<Boolean> = repository.getLoading()
    val refresh: MutableLiveData<Boolean> = MutableLiveData()
    val pies: LiveData<List<BakedPie>> = Transformations.map(repository.getPies()) { bakePies(it) }

    val urlAction = ActionLiveData<String>()

    private fun bakePies(pies: List<RawPie>) = pies.map {
        BakedPie(
            it.pie.user.avatarUrl,
            it.pie.user.name,
            "@${it.pie.user.handle}",
            it.pie.user.protected,
            it.pie.user.verified,
            formatTimestamp(it.pie.createdAt),
            formatInfo(it.pie),
            formatText(it.pie.text),
            it.pie.retweeted,
            formatNumber(it.pie.retweetCount),
            it.pie.favorited,
            formatNumber(it.pie.favoriteCount),
            formatNumber(it.pie.score),
            formatUserUrl(it.pie.user.handle),
            formatTweetUrl(it.pie.user.handle, it.pie.pieId)
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