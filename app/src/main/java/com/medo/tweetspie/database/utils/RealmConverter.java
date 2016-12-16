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
import com.medo.tweetspie.utils.ScoreUtils;
import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.TweetEntities;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.models.VideoInfo;

import java.util.List;

import io.realm.RealmList;


public class RealmConverter {

  @NonNull
  public static RealmTweet convertTweet(@NonNull Tweet tweet) {

    if (tweet.retweetedStatus == null) {
      return convertTweet(tweet, null);
    }
    else {
      return convertTweet(tweet.retweetedStatus, tweet.user);
    }
  }

  private static RealmTweet convertTweet(@NonNull Tweet tweet, @Nullable User retweetedBy) {

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
    final int score = ScoreUtils.rateTweet(realmTweet);
    realmTweet.setScore(score);
    return realmTweet;
  }

  @NonNull
  public static RealmFriendId convertFriendId(@NonNull Long friendId) {

    RealmFriendId realmFriendId = new RealmFriendId();
    realmFriendId.setId(friendId);
    return realmFriendId;
  }

  @Nullable
  private static RealmList<RealmTweetEntity> extractMediaEntities(@Nullable TweetEntities entities) {

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
  private static RealmTweetUser extractUser(@Nullable User user) {

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

    return realmTweetUser;
  }

  @Nullable
  private static String extractVideoUrl(@NonNull MediaEntity entity) {

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
}
