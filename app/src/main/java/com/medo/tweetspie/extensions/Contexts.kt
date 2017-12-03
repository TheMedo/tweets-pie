package com.medo.tweetspie.extensions

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager
import android.widget.Toast

fun Context.toast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.register(receiver: BroadcastReceiver, intentFilter: IntentFilter) =
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter)

fun Context.unregister(receiver: BroadcastReceiver) =
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)

fun Context.send(intent: Intent) = LocalBroadcastManager.getInstance(this).sendBroadcast(intent)