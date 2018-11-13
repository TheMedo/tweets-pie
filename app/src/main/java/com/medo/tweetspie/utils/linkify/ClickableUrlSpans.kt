package com.medo.tweetspie.utils.linkify

import android.text.TextPaint
import android.text.style.URLSpan
import android.view.View

class ClickableUrlSpan(
    private val color: Int = 0,
    private val url: String = "",
    private val action: (String) -> Unit = {}
) : URLSpan(url) {

    override fun onClick(widget: View) {
        action.invoke(url)
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.color = color
        ds.isUnderlineText = false
    }
}