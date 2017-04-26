package com.medo.tweetspie.database.utils;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.medo.tweetspie.database.model.RealmFriendId;
import com.medo.tweetspie.database.model.RealmTweet;
import com.medo.tweetspie.database.model.RealmTweetEntity;
import com.medo.tweetspie.database.model.RealmTweetUser;
import com.medo.tweetspie.utils.Constant;
import com.medo.tweetspie.utils.FormatUtils;
import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.TweetEntities;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.models.VideoInfo;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.realm.RealmList;


public class RealmConverter {

  @NonNull
  public RealmTweet convertTweet(@NonNull Tweet tweet) {

    if (tweet.retweetedStatus == null) {
      return convertTweet(tweet, null);
    }
    else {
      return convertTweet(tweet.retweetedStatus, tweet.user);
    }
  }

  private RealmTweet convertTweet(@NonNull Tweet tweet, @Nullable User retweetedBy) {

    RealmTweet realmTweet = new RealmTweet();
    if (tweet.place != null) {
      realmTweet.setCountryCode(tweet.place.countryCode);
    }
    realmTweet.setCreatedAt(FormatUtils.utcToDate(tweet.createdAt));
    realmTweet.setEntities(extractMediaEntities(tweet.entities));
    realmTweet.setExtendedEntities(extractMediaEntities(tweet.extendedEtities));
    realmTweet.setFavoriteCount(tweet.favoriteCount);
    realmTweet.setFavorited(tweet.favorited);
    realmTweet.setIdStr(tweet.idStr);
    realmTweet.setRetweetCount(tweet.retweetCount);
    realmTweet.setRetweeted(tweet.retweeted);
    realmTweet.setText(tweet.text);
    realmTweet.setUser(extractUser(tweet.user));
    realmTweet.setRetweetedBy(extractUser(retweetedBy));
    // calculate the score
    final int score = rateTweet(realmTweet);
    realmTweet.setScore(score);
    return realmTweet;
  }

  @NonNull
  public RealmFriendId convertFriendId(@NonNull Long friendId) {

    RealmFriendId realmFriendId = new RealmFriendId();
    realmFriendId.setId(friendId);
    return realmFriendId;
  }

  @Nullable
  private RealmList<RealmTweetEntity> extractMediaEntities(@Nullable TweetEntities entities) {

    if (entities == null || entities.media == null || entities.media.isEmpty()) {
      // there is no media to extract
      return null;
    }
    RealmList<RealmTweetEntity> realmTweetEntities = new RealmList<>();
    for (MediaEntity mediaEntity : entities.media) {
      // convert all media entities to realm entities
      RealmTweetEntity realmTweetEntity = new RealmTweetEntity();
      realmTweetEntity.setMediaUrl(mediaEntity.mediaUrl);
      realmTweetEntity.setVideoUrl(extractVideoUrl(mediaEntity));
      realmTweetEntity.setType(mediaEntity.type);
      realmTweetEntities.add(realmTweetEntity);
    }

    return realmTweetEntities;
  }

  @Nullable
  private RealmTweetUser extractUser(@Nullable User user) {

    if (user == null) {
      return null;
    }
    RealmTweetUser realmTweetUser = new RealmTweetUser();
    realmTweetUser.setId(user.getId());
    realmTweetUser.setFollowersCount(user.followersCount);
    realmTweetUser.setName(user.name);
    realmTweetUser.setProfileBackgroundColor(user.profileBackgroundColor);
    realmTweetUser.setProfileImageUrl(user.profileImageUrl);
    realmTweetUser.setProtectedUser(user.protectedUser);
    realmTweetUser.setScreenName(user.screenName);
    realmTweetUser.setLocked(user.protectedUser);

    return realmTweetUser;
  }

  @Nullable
  private String extractVideoUrl(@NonNull MediaEntity entity) {

    final VideoInfo videoInfo = entity.videoInfo;
    if (videoInfo != null) {
      // we have a video info that contains at least one variant
      // obtain the url to the video
      final List<VideoInfo.Variant> variants = videoInfo.variants;
      if (variants != null && !variants.isEmpty()) {
        for (VideoInfo.Variant variant : variants) {
          if (TextUtils.equals(Constant.VIDEO_MP4, variant.contentType)) {
            // we are only interested in the .mp4 video variant
            return variant.url;
          }
        }
      }
    }
    return null;
  }

  private int rateTweet(@NonNull RealmTweet tweet) {

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
    score += new Random().nextInt(20);

    return score;
  }

  /**
   * Returns the time score based on the country
   *
   * @param countryCode the country code where the tweet was created
   * @return 20 if matches the user country code, 0 otherwise
   */
  private int getCountryScore(@Nullable String countryCode) {

    if (countryCode == null) {
      return 0;
    }
    return !countryCode.equalsIgnoreCase(Locale.getDefault().getCountry()) ? 0 : 20;
  }

  /**
   * Returns the time score based on the tweet recency to now
   *
   * @param createdAt the date the tweet was created at
   * @return the recency score in the range of [-50, 50]
   */
  private int getRecencyScore(@Nullable Date createdAt) {

    if (createdAt == null) {
      // date cannot be parsed
      return 0;
    }
    final long createdAtMillis = createdAt.getTime();
    final long currentTimeMillis = System.currentTimeMillis();
    final long diffInMillis = currentTimeMillis - createdAtMillis;
    if (diffInMillis > TimeUnit.DAYS.toMillis(2)) {
      // tweets older than a two days should not be rated very low
      return -50;
    }
    if (diffInMillis > TimeUnit.DAYS.toMillis(1)) {
      // tweets older than a day should not be rated high
      return -10;
    }
    // normalize the recency in range
    return (int) (1.0f - normalize(diffInMillis, 0, TimeUnit.DAYS.toMillis(1))) * 50;
  }

  /**
   * Returns the time zone score based on proximity to the same time zone.
   * The same time zone gets a score of 20, the +/-12 time zone gets score of 0.
   *
   * @param createdAt the date the tweet was created at
   * @return the time zone score in the range of [0, 20]
   */
  private int getTimeZoneScore(@NonNull Date createdAt) {

    final Calendar nowCal = Calendar.getInstance();
    final Calendar createdAtCal = Calendar.getInstance();
    createdAtCal.setTime(createdAt);

    final long nowOffset = (nowCal.get(Calendar.ZONE_OFFSET) + nowCal.get(Calendar.DST_OFFSET));
    final long createdAtOffset = (createdAtCal.get(Calendar.ZONE_OFFSET) + createdAtCal.get(Calendar.DST_OFFSET));
    final long diffHours = Math.abs(TimeUnit.MILLISECONDS.toHours(nowOffset - createdAtOffset));

    return (int) normalize(diffHours, 0, 12) * 20;
  }

  /**
   * Returns the retweet score based on the retweet count and user's followers count.
   * It is normalized in the range of [0, 10]
   *
   * @param retweetCount   the tweet retweet count
   * @param followersCount the tweet author followers count
   * @return the retweet score between 0 and 10
   */
  private int getRetweetScore(int retweetCount, int followersCount) {

    return Math.min(10, (int) ((float) retweetCount / followersCount * 2000));
  }

  /**
   * Returns the favorite score based on the favorite count and user's followers count.
   * It is normalized in the range of [0, 10]
   *
   * @param favoriteCount  the tweet favorite count
   * @param followersCount the tweet author followers count
   * @return the retweet score between 0 and 10
   */
  private int getFavoriteScore(int favoriteCount, int followersCount) {

    return Math.min(10, (int) ((float) favoriteCount / followersCount * 1000));
  }

  /**
   * Returns the media score based on the media presence
   *
   * @param entities         the media entities contained in the tweet
   * @param extendedEntities the extended media entities contained in the tweet
   * @return 0, 5 or 10 based on the media presence
   */
  private int getMediaScore(@Nullable RealmList<RealmTweetEntity> entities,
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
   * @return 0 if the user is not following the tweet author, 30 otherwise
   */
  private int getFriendsScore(boolean isFriend) {

    return !isFriend ? 0 : 30;
  }

  /**
   * Normalizes the input value from a given range into the [0.0, 1.0] range
   *
   * @param valueIn the input value
   * @param baseMin the starting range min
   * @param baseMax the starting range max
   * @return the normalized number in range
   */
  public float normalize(final long valueIn, final long baseMin, final long baseMax) {

    return (valueIn - baseMin) / (float) (baseMax - baseMin);
  }
}
