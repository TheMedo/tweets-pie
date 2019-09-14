package com.medo.tweetspie.utils

import com.medo.tweetspie.R
import com.medo.tweetspie.data.local.model.Pie
import com.medo.tweetspie.data.local.model.RawPie
import com.medo.tweetspie.system.Formatter
import com.medo.tweetspie.system.Resources
import com.medo.tweetspie.ui.main.BakedPie
import com.medo.tweetspie.util.linkify.TwitterLinkify

interface PieBaker {
    fun bakePies(pies: List<RawPie>, urlAction: (String) -> Unit = {}): List<BakedPie>
}

class PieBakerImpl(
    private val formatter: Formatter,
    private val resources: Resources,
    private val linkify: TwitterLinkify
) : PieBaker {

    override fun bakePies(pies: List<RawPie>, urlAction: (String) -> Unit) = pies.map {
        BakedPie(
            it.pie.user.avatarUrl,
            it.pie.user.name,
            "@${it.pie.user.handle}",
            it.pie.user.protected,
            it.pie.user.verified,
            formatTimestamp(it.pie.createdAt),
            formatInfo(it.pie),
            formatText(it.pie.text, urlAction),
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
        if (retweet != null)
            builder.appendln(resources.getString(R.string.hint_retweet, "@$retweet"))
        val reply = pie.inReplyTo
        if (reply != null)
            builder.appendln(resources.getString(R.string.hint_reply, reply))
        val quote = pie.quoted
        if (quote != null)
            builder.appendln(resources.getString(R.string.hint_quote, quote))
        return builder.trim().toString()
    }

    private fun formatText(text: String, urlAction: (String) -> Unit) =
        linkify.linkifyText(text, resources.getColor(R.color.colorPrimaryDark), urlAction)

    private fun formatNumber(count: Int) =
        formatter.formatNumber(count.toLong())

    private fun formatTweetUrl(handle: String, id: String) =
        resources.getString(R.string.url_tweet, handle, id)

    private fun formatUserUrl(handle: String) =
        resources.getString(R.string.url_user, handle)
}