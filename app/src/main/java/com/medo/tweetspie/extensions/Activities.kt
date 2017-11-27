package com.medo.tweetspie.extensions

import android.app.Activity
import android.content.Intent

inline fun <reified T : Any> Activity.launchActivity(requestCode: Int = -1, noinline setup: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    intent.setup()
    startActivityForResult(intent, requestCode)
}