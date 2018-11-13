package com.medo.tweetspie.utils.linkify

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.URLSpan
import android.util.Patterns
import java.util.regex.Pattern

interface TwitterLinkify {

    fun linkifyText(
        text: String = "",
        color: Int = Color.BLUE,
        action: (String) -> Unit = {}
    ): Spanned
}

class TwitterLinkifyImpl : TwitterLinkify {

    private val patterns = listOf<Pattern>(
        Patterns.WEB_URL,
        Pattern.compile("#(\\w+)"),
        Pattern.compile("@(\\w+)")
    )

    override fun linkifyText(
        text: String,
        color: Int,
        action: (String) -> Unit
    ): Spanned {
        val spannable = SpannableString(text)
        patterns
            .mapNotNull { it.matcher(spannable) }
            .forEach {
                while (it.find()) {
                    val start = it.start()
                    val end = it.end()
                    val spanText = text.substring(start, end)
                    val spans = spannable.getSpans(start, end, URLSpan::class.java)
                    if (spans.isEmpty()) {
                        val urlSpan = ClickableUrlSpan(color, spanText, action)
                        spannable.setSpan(urlSpan, start, end, 0)
                    }
                }
            }
        return spannable
    }
}