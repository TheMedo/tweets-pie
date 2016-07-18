package com.medo.tweetspie.database.utils;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.medo.tweetspie.database.model.RealmFriendId;
import com.medo.tweetspie.database.model.RealmTweet;
import com.medo.tweetspie.database.model.RealmTweetEntity;
import com.medo.tweetspie.database.model.RealmTweetUser;
import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.TweetEntities;
import com.twitter.sdk.android.core.models.User;

import io.realm.RealmList;


public class RealmConverter {

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
    realmTweetUser.setFollowersCount(user.followersCount);
    realmTweetUser.setName(user.name);
    realmTweetUser.setProfileBackgroundColor(user.profileBackgroundColor);
    realmTweetUser.setProfileImageUrl(user.profileImageUrl);
    realmTweetUser.setProtectedUser(user.protectedUser);
    realmTweetUser.setScreenName(user.screenName);

    return realmTweetUser;
  }

  @Nullable
  public static RealmTweet convertTweet(@NonNull Tweet tweet) {

    RealmTweet realmTweet = new RealmTweet();
    if (tweet.place != null) {
      realmTweet.setCountryCode(tweet.place.countryCode);
    }
    realmTweet.setCreatedAt(tweet.createdAt);
    realmTweet.setEntities(extractMediaEntities(tweet.entities));
    realmTweet.setExtendedEntities(extractMediaEntities(tweet.extendedEtities));
    realmTweet.setFavoriteCount(tweet.favoriteCount);
    realmTweet.setFavorited(tweet.favorited);
    realmTweet.setIdStr(tweet.idStr);
    realmTweet.setRetweetCount(tweet.retweetCount);
    realmTweet.setRetweeted(tweet.retweeted);
    realmTweet.setScore(0);
    realmTweet.setText(tweet.text);
    realmTweet.setUser(extractUser(tweet.user));

    return realmTweet;
  }

  @NonNull
  public static RealmFriendId convertFriendId(@NonNull Long friendId) {

    RealmFriendId realmFriendId = new RealmFriendId();
    realmFriendId.setId(friendId);
    return realmFriendId;
  }
}
