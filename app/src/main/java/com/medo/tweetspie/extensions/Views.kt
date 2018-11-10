package com.medo.tweetspie.extensions

import android.view.View

fun View.show(visible: Boolean = true, gone: Boolean = true) {
    visibility = if (visible) View.VISIBLE else if (gone) View.GONE else View.INVISIBLE
}