package com.medo.tweetspie.service

import com.medo.tweetspie.base.AbsBasePresenter
import com.medo.tweetspie.system.LAST_FRIENDS_IDS_TIMESTAMP
import com.medo.tweetspie.system.LAST_UPDATE_TIMESTAMP
import com.medo.tweetspie.system.Preferences
import com.medo.tweetspie.system.USERNAME
import com.medo.tweetspie.twitter.TwitterClient
import com.medo.tweetspie.utils.FRIENDS_IDS_TIMEOUT
import com.medo.tweetspie.utils.TWEETS_TIMEOUT

class ServicePresenter(
        private val twitterClient: TwitterClient,
        private val preferences: Preferences
) : AbsBasePresenter<ServiceMvp.View>(), ServiceMvp.Presenter {

    override fun onAttach(view: ServiceMvp.View) {
        super.onAttach(view)
        val connected = twitterClient.checkSession()
        if (!connected) {
            preferences.clear()
            view.showErrorNotification()
            view.exit()
            return
        }

        val currentTime = System.currentTimeMillis()
        val lastFriendsIdsTimestamp = preferences.getLong(LAST_FRIENDS_IDS_TIMESTAMP)
        if (currentTime - lastFriendsIdsTimestamp > FRIENDS_IDS_TIMEOUT) {
            val friendsIds = twitterClient.getFriendsIds(preferences.getString(USERNAME))
            if (friendsIds.isNotEmpty()) {
                // TODO persist this
                preferences[LAST_FRIENDS_IDS_TIMESTAMP] = currentTime
            }
        }

        val lastUpdateTimestamp = preferences.getLong(LAST_UPDATE_TIMESTAMP)
        if (currentTime - lastUpdateTimestamp > TWEETS_TIMEOUT) {
            val tweets = twitterClient.getTimeline()
            if (tweets.isNotEmpty()) {
                // TODO persist this
                preferences[LAST_UPDATE_TIMESTAMP] = currentTime
                view.sendUpdateBroadcast(true)
            } else {
                view.sendUpdateBroadcast(false)
            }
        }
        view.exit()
    }
}