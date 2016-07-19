package com.medo.tweetspie.utils;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.medo.tweetspie.database.model.RealmTweet;
import com.medo.tweetspie.database.model.RealmTweetEntity;
import com.medo.tweetspie.database.model.RealmTweetUser;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.realm.RealmList;


public class ScoreUtils {

  private static final Random random = new Random();

  public static int rateTweet(@NonNull RealmTweet tweet) {

    int score = 0;
    final RealmTweetUser user = tweet.getUser();
    // add to score based on recency
    score += getRecencyScore(tweet.getCreatedAt());
    // add to score based on country
    score += getCountryScore(tweet.getCountryCode());
    // add to score based on time zone
    score += getTimeZoneScore(tweet.getCreatedAt());
    // add to score based on retweets
    score += getRetweetScore(tweet.getRetweetCount(), user.getFollowersCount());
    // add to score based on favorites
    score += getFavoriteScore(tweet.getFavoriteCount(), user.getFollowersCount());
    // add to score based on media presence
    score += getMediaScore(tweet.getEntities(), tweet.getExtendedEntities());
    // add to score based on friendship
    score += getFriendsScore(user.isFriend());
    // TODO add hashtag blacklist
    // TODO add hidden before
    // TODO add liked before
    // add to score a random factor
    score += random.nextInt(10);

    return score;
  }

  /**
   * Returns the time score based on the country
   *
   * @param countryCode the country code where the tweet was created
   * @return 10 if matches the user country code, 0 otherwise
   */
  private static int getCountryScore(@Nullable String countryCode) {

    if (countryCode == null) {
      return 0;
    }
    return !countryCode.equalsIgnoreCase(Locale.getDefault().getCountry()) ? 0 : 10;
  }

  /**
   * Returns the time score based on the tweet recency to now
   *
   * @param createdAt the date the tweet was created at
   * @return the recency score in the range of [0, 10]
   */
  private static int getRecencyScore(@Nullable Date createdAt) {

    if (createdAt == null) {
      // date cannot be parsed
      return 0;
    }
    final long createdAtMillis = createdAt.getTime();
    final long currentTimeMillis = System.currentTimeMillis();
    final long diffInMillis = currentTimeMillis - createdAtMillis;
    if (diffInMillis > TimeUnit.DAYS.toMillis(1)) {
      // tweets older than a day should not be rated
      return 0;
    }
    // normalize the recency in range
    return (int) MathUtils.scaleInRange(diffInMillis, createdAtMillis, currentTimeMillis, 0, 10);
  }

  /**
   * Returns the time zone score based on proximity to the same time zone.
   * The same time zone gets a score of 10, the +/-12 time zone gets score of 0.
   *
   * @param createdAt the date the tweet was created at
   * @return the time zone score in the range of [0, 10]
   */
  private static int getTimeZoneScore(@NonNull Date createdAt) {

    final Calendar nowCal = Calendar.getInstance();
    final Calendar createdAtCal = Calendar.getInstance();
    createdAtCal.setTime(createdAt);

    final long nowOffset = (nowCal.get(Calendar.ZONE_OFFSET) + nowCal.get(Calendar.DST_OFFSET));
    final long createdAtOffset = (createdAtCal.get(Calendar.ZONE_OFFSET) + createdAtCal.get(Calendar.DST_OFFSET));
    final long diffHours = Math.abs(TimeUnit.MILLISECONDS.toHours(nowOffset - createdAtOffset));

    return (int) MathUtils.scaleInRange(diffHours, 0, 12, 10, 0);
  }

  /**
   * Returns the retweet score based on the retweet count and user's followers count.
   * It is normalized in the range of [0, 10]
   *
   * @param retweetCount   the tweet retweet count
   * @param followersCount the tweet author followers count
   * @return the retweet score between 0 and 10
   */
  private static int getRetweetScore(int retweetCount, int followersCount) {

    return Math.min(10, retweetCount / followersCount * 2000);
  }

  /**
   * Returns the favorite score based on the favorite count and user's followers count.
   * It is normalized in the range of [0, 10]
   *
   * @param favoriteCount  the tweet favorite count
   * @param followersCount the tweet author followers count
   * @return the retweet score between 0 and 10
   */
  private static int getFavoriteScore(int favoriteCount, int followersCount) {

    return Math.min(10, favoriteCount / followersCount * 1000);
  }

  /**
   * Returns the media score based on the media presence
   *
   * @param entities         the media entities contained in the tweet
   * @param extendedEntities the extended media entities contained in the tweet
   * @return 0, 5 or 10 based on the media presence
   */
  private static int getMediaScore(@Nullable RealmList<RealmTweetEntity> entities,
                                   @Nullable RealmList<RealmTweetEntity> extendedEntities) {

    int mediaScore = 0;
    mediaScore += (entities == null || entities.isEmpty()) ? 0 : 5;
    mediaScore += (extendedEntities == null || extendedEntities.isEmpty()) ? 0 : 5;
    return mediaScore;
  }

  /**
   * Returns the friend score based on the following status
   * i.e. the user follow the tweet author
   *
   * @param isFriend the friend status
   * @return 0 if the user is not following the tweet author, 10 otherwise
   */
  private static int getFriendsScore(boolean isFriend) {

    return !isFriend ? 0 : 10;
  }
}
