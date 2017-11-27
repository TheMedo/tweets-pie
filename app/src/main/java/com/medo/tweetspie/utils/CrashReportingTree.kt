package com.medo.tweetspie.utils

import android.util.Log

import timber.log.Timber

class CrashReportingTree : Timber.Tree() {

    override fun log(priority: Int, tag: String, message: String, t: Throwable) {
        when (priority) {
        // ignore debug tags
            Log.VERBOSE, Log.DEBUG -> return
            else -> {
//                FakeCrashLibrary.log(priority, tag, message)
//                when (priority) {
//                    Log.ERROR -> FakeCrashLibrary.logError(t)
//                    Log.WARN -> FakeCrashLibrary.logWarning(t)
//                }
            }
        }
    }
}