package com.medo.tweetspie.extensions

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

fun AppCompatActivity.snack(message: String? = null, duration: Int = Snackbar.LENGTH_SHORT) {
    message ?: return
    val content = findViewById<View>(android.R.id.content) ?: return
    Snackbar.make(content, message, duration).show()
}