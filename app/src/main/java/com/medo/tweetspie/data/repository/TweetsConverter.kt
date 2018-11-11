package com.medo.tweetspie.data.repository

import androidx.annotation.Nullable
import com.medo.tweetspie.data.local.PieDao
import com.medo.tweetspie.data.local.model.Pie
import com.medo.tweetspie.data.local.model.PieUser
import com.medo.tweetspie.system.Clock
import com.medo.tweetspie.utils.Formatter
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.models.User
import java.util.Calendar
import java.util.Locale
import java.util.Random

interface TweetsConverter {

    fun convertTweets(remoteTweets: List<Tweet>): List<Pie>
}

class TweetsConverterImpl(
    private val clock: Clock,
    private val formatter: Formatter,
    private val pieDao: PieDao
) : TweetsConverter {

    override fun convertTweets(remoteTweets: List<Tweet>) = remoteTweets.map {
        convertTweet(
            it.retweetedStatus ?: it,
            if (it.retweetedStatus != null) it.user.screenName else null
        )
    }

    private fun convertTweet(tweet: Tweet, retweetedBy: String?) = Pie(
        pieId = tweet.idStr,
        text = tweet.text,
        createdAt = tweet.createdAt,
        favoriteCount = tweet.favoriteCount,
        favorited = tweet.favorited,
        retweetCount = tweet.retweetCount,
        retweeted = tweet.retweeted,
        language = tweet.lang,
        country = tweet.place?.country,
        source = tweet.source,
        inReplyTo = tweet.inReplyToScreenName,
        retweetedBy = retweetedBy,
        quoted = tweet.quotedStatus?.user?.screenName,
        user = convertUser(tweet.user),
        score = calculateScore(tweet)
    )

    private fun convertUser(user: User) = PieUser(
        userId = user.idStr,
        name = user.name,
        avatarUrl = user.profileImageUrl,
        handle = user.screenName,
        protected = user.protectedUser,
        verified = user.verified,
        followingCount = user.friendsCount,
        followersCount = user.followersCount,
        tweetsCount = user.statusesCount
    )

    private fun calculateScore(tweet: Tweet): Int {
        var score = 0
        // add to score based on recency
        score += getRecencyScore(tweet.createdAt)
        // add to score based on country
        score += getCountryScore(tweet.place?.country)
        // add to score based on time zone
        score += getTimeZoneScore(tweet.createdAt)
        // add to score based on retweets
        score += getRetweetScore(tweet.retweetCount, tweet.user.followersCount)
        // add to score based on favorites
        score += getFavoriteScore(tweet.favoriteCount, tweet.user.followersCount)
        // TODO add to score based on media presence
        // score += getMediaScore(tweet.getEntities(), tweet.getExtendedEntities())
        // TODO add to score based on friendship
        score += getFriendsScore(tweet.user.idStr)
        // TODO add hashtag blacklist
        // TODO add hidden before
        // TODO add liked before
        // add to score a random factor
        score += Random().nextInt(20)

        return score
    }

    /**
     * Returns the time score based on the country
     *
     * @param countryCode the country code where the tweet was created
     * @return 20 if matches the user country code, 0 otherwise
     */
    private fun getCountryScore(@Nullable countryCode: String?): Int {

        if (countryCode == null) {
            return 0
        }
        return if (!countryCode.equals(Locale.getDefault().country, ignoreCase = true)) 0 else 20
    }

    /**
     * Returns the time score based on the tweet recency to now
     *
     * @param createdAt the date the tweet was created at
     * @return the recency score in the range of [-50, 50]
     */
    private fun getRecencyScore(createdAt: String): Int {
        val createdAtMillis = formatter.utcToDate(createdAt).time
        val currentTimeMillis = clock.getCurrentTime()
        val deltaMillis = currentTimeMillis - createdAtMillis
        return when {
            deltaMillis > clock.daysToMillis(2) -> -50
            deltaMillis > clock.daysToMillis(1) -> -10
            else -> ((1.0f - normalize(deltaMillis, 0, clock.daysToMillis(1))) * 50).toInt()
        }
    }

    /**
     * Returns the time zone score based on proximity to the same time zone.
     * The same time zone gets a score of 20, the +/-12 time zone gets score of 0.
     *
     * @param createdAt the date the tweet was created at
     * @return the time zone score in the range of [0, 20]
     */
    private fun getTimeZoneScore(createdAt: String): Int {
        val calendar = Calendar.getInstance().apply {
            time = formatter.utcToDate(createdAt)
        }
        val diffHours = clock.timezoneOffset(calendar)
        return (normalize(diffHours, 0, 12) * 20).toInt()
    }

    /**
     * Returns the retweet score based on the retweet count and user's followers count.
     * It is normalized in the range of [0, 10]
     *
     * @param retweetCount   the tweet retweet count
     * @param followersCount the tweet author followers count
     * @return the retweet score between 0 and 10
     */
    private fun getRetweetScore(retweetCount: Int, followersCount: Int) =
        Math.min(10, (retweetCount.toFloat() / followersCount * 2000).toInt())

    /**
     * Returns the favorite score based on the favorite count and user's followers count.
     * It is normalized in the range of [0, 10]
     *
     * @param favoriteCount  the tweet favorite count
     * @param followersCount the tweet author followers count
     * @return the retweet score between 0 and 10
     */
    private fun getFavoriteScore(favoriteCount: Int, followersCount: Int) =
        Math.min(10, (favoriteCount.toFloat() / followersCount * 1000).toInt())

//    /**
//     * Returns the media score based on the media presence
//     *
//     * @param entities         the media entities contained in the tweet
//     * @param extendedEntities the extended media entities contained in the tweet
//     * @return 0, 5 or 10 based on the media presence
//     */
//    private fun getMediaScore(
//        @Nullable entities: RealmList<RealmTweetEntity>?,
//        @Nullable extendedEntities: RealmList<RealmTweetEntity>?
//    ): Int {
//
//        var mediaScore = 0
//        mediaScore += if (entities == null || entities!!.isEmpty()) 0 else 5
//        mediaScore += if (extendedEntities == null || extendedEntities!!.isEmpty()) 0 else 5
//        return mediaScore
//    }

    /**
     * Returns the friend score dependant on the current user following the provided user
     *
     * @param userId the user id to check
     * @return 0 if the user is not following the user, 30 otherwise
     */
    private fun getFriendsScore(userId: String): Int {
        val isFriend = pieDao.getFriend(userId) != null
        return if (isFriend) 30 else 0
    }

    /**
     * Normalizes the input value from a given range into the [0.0, 1.0] range
     *
     * @param valueIn the input value
     * @param baseMin the starting range min
     * @param baseMax the starting range max
     * @return the normalized number in range
     */
    private fun normalize(valueIn: Long, baseMin: Long, baseMax: Long) =
        (valueIn - baseMin) / (baseMax - baseMin).toFloat()
}