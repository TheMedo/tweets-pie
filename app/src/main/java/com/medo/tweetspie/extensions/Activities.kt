package com.medo.tweetspie.extensions

import android.app.Activity
import android.view.View
import com.google.android.material.snackbar.Snackbar

fun Activity.snack(message: String? = null, duration: Int = Snackbar.LENGTH_SHORT) {
    message ?: return
    val content = findViewById<View>(android.R.id.content) ?: return
    Snackbar.make(content, message, duration).show()
}