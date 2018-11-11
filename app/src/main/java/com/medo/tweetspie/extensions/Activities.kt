package com.medo.tweetspie.extensions

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.core.net.toUri
import com.google.android.material.snackbar.Snackbar

fun Activity.snack(message: String? = null, duration: Int = Snackbar.LENGTH_SHORT) {
    message ?: return
    val content = findViewById<View>(android.R.id.content) ?: return
    Snackbar.make(content, message, duration).show()
}

fun Activity.openUrl(url: String = "") = startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))