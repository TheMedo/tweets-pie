package com.medo.tweetspie.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import com.medo.tweetspie.R
import com.medo.tweetspie.base.BaseService
import com.medo.tweetspie.di.BindContext
import com.medo.tweetspie.extensions.send
import com.medo.tweetspie.main.MainActivity
import com.medo.tweetspie.utils.ACTION_UPDATE
import com.medo.tweetspie.utils.EXTRA_SUCCESS
import com.medo.tweetspie.utils.NOTIFICATIONS_CHANNEL_ID
import org.koin.android.ext.android.inject

class MainService : BaseService("MainService"), ServiceMvp.View {

    override val contextName: String = BindContext.Service
    override val presenter by inject<ServiceMvp.Presenter>()

    override fun onHandleIntent(intent: Intent?) {
        presenter.onAttach(this)
    }

    override fun showErrorNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createNotificationsChannel()

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        createBuilder().setContentTitle(getString(R.string.error_notification_twitter_title))
                .setContentText(getString(R.string.error_notification_twitter_message))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()
    }

    override fun sendUpdateBroadcast(success: Boolean) {
        val intent = Intent(ACTION_UPDATE)
        intent.putExtra(EXTRA_SUCCESS, success)
        send(intent)
    }

    override fun exit() = stopSelf()

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationsChannel() {
        val manager = getSystemService(android.content.Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
                NOTIFICATIONS_CHANNEL_ID,
                getString(R.string.notification_channel_tweets),
                NotificationManager.IMPORTANCE_LOW)
        manager.createNotificationChannel(channel)
    }

    private fun createBuilder(): Notification.Builder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            Notification.Builder(applicationContext, NOTIFICATIONS_CHANNEL_ID)
        else
            Notification.Builder(applicationContext)
    }
}