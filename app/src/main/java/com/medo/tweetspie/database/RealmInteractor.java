package com.medo.tweetspie.database;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.medo.tweetspie.database.model.RealmFriendId;
import com.medo.tweetspie.database.model.RealmTweet;
import com.medo.tweetspie.database.model.RealmTweetUser;
import com.medo.tweetspie.database.utils.RealmConverter;
import com.medo.tweetspie.system.PreferencesProvider;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.Sort;
import timber.log.Timber;


public class RealmInteractor implements RealmTransaction {

  private final PreferencesProvider preferences;
  private final RealmConverter converter;
  private final Realm realm;

  public RealmInteractor(PreferencesProvider preferencesProvider, RealmConverter converter) {

    this.preferences = preferencesProvider;
    this.converter = converter;
    this.realm = Realm.getDefaultInstance();
  }

  public static void init(@NonNull Context context) {
    // create a default realm configuration that will delete the database if migration is needed
    // this will remove the migration overhead when modifying the database schema
    Realm.init(context);
    RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build();
    Realm.setDefaultConfiguration(realmConfiguration);
  }

  @Override
  public void onDestroy() {

    realm.close();
  }

  @Override
  public void persistTweets(@NonNull List<Tweet> tweets) {
    // convert all tweets to realm objects
    List<RealmTweet> convertedTweets = convertAndRateTweets(tweets);
    List<RealmTweet> sortedTweets = sortAndTrimTweets(convertedTweets);
    updateUserFriends(sortedTweets);
    deleteOldAndPersistNewTweets(sortedTweets);
  }

  @Override
  public void persistFriendsIds(@NonNull List<Long> friendsIds) {
    // convert all tweets to realm objects
    List<RealmFriendId> realmFriendIds = new ArrayList<>(friendsIds.size());
    for (Long id : friendsIds) {
      realmFriendIds.add(converter.convertFriendId(id));
    }
    // persist the realm tweets
    realm.beginTransaction();
    realm.copyToRealmOrUpdate(realmFriendIds);
    realm.commitTransaction();

    Timber.d("Persisted %d friend ids", realmFriendIds.size());
  }

  @NonNull
  public OrderedRealmCollection<RealmTweet> getTweets() {

    return realm.where(RealmTweet.class).findAllSorted("score", Sort.DESCENDING);
  }

  @Nullable
  public RealmTweet getTweet(@NonNull String id) {

    return realm.where(RealmTweet.class).equalTo("idStr", id).findFirst();
  }

  @Override
  public boolean hasFriendsIds() {

    return realm.where(RealmFriendId.class).findFirst() != null;
  }

  @Override
  public boolean toggleRetweet(@NonNull String id) {

    RealmTweet tweet = getRealmTweet(id);
    if (tweet == null) {
      return false;
    }
    final boolean isRetweeted = tweet.isRetweeted();
    final int retweetCount = tweet.getRetweetCount();
    realm.beginTransaction();
    tweet.setRetweeted(!isRetweeted);
    tweet.setRetweetCount(isRetweeted ? retweetCount - 1 : retweetCount + 1);
    realm.commitTransaction();

    return tweet.isRetweeted();
  }

  @Override
  public boolean toggleFavorite(@NonNull String id) {

    RealmTweet tweet = getRealmTweet(id);
    if (tweet == null) {
      return false;
    }
    final boolean isFavorited = tweet.isFavorited();
    final int favoriteCount = tweet.getFavoriteCount();
    realm.beginTransaction();
    tweet.setFavorited(!isFavorited);
    tweet.setFavoriteCount(isFavorited ? favoriteCount - 1 : favoriteCount + 1);
    realm.commitTransaction();

    return tweet.isFavorited();
  }

  @NonNull
  private List<RealmTweet> convertAndRateTweets(@NonNull List<Tweet> tweets) {
    // convert all tweets to realm objects
    List<RealmTweet> realmTweets = new ArrayList<>(tweets.size());
    final boolean showRetweets = preferences.getBoolean(PreferencesProvider.RETWEETS);
    final boolean showReplies = preferences.getBoolean(PreferencesProvider.REPLIES);

    for (Tweet tweet : tweets) {
      if (!showRetweets && tweet.retweetedStatus != null) {
        // we should not show retweets
        continue;
      }
      if (!showReplies && tweet.inReplyToScreenName != null) {
        // we should not show replies
        continue;
      }
      // convert and add the tweet
      realmTweets.add(converter.convertTweet(tweet));
    }
    return realmTweets;
  }

  @NonNull
  private List<RealmTweet> sortAndTrimTweets(@NonNull List<RealmTweet> tweets) {
    // sort the list based on the score in descending order
    Collections.sort(tweets, (leftTweet, rightTweet) -> Integer.compare(rightTweet.getScore(), leftTweet.getScore()));
    // return a subset of the highest rated tweets
    return tweets.subList(0, (int) preferences.getLong(PreferencesProvider.MAX_TWEETS));
  }

  private void updateUserFriends(@NonNull List<RealmTweet> tweets) {
    // update the friends flag if the current user and the tweet author are friends
    for (RealmTweet tweet : tweets) {
      final RealmTweetUser user = tweet.getUser();
      user.setFriend(isFriend(user.getId()));
    }
  }

  private void deleteOldAndPersistNewTweets(@NonNull List<RealmTweet> tweets) {
    // persist the realm tweets
    realm.beginTransaction();
    realm.delete(RealmTweet.class);
    realm.copyToRealmOrUpdate(tweets);
    realm.commitTransaction();

    Timber.d("Persisted %d tweets", tweets.size());
  }

  private boolean isFriend(long id) {

    return realm.where(RealmFriendId.class).equalTo("id", id).findFirst() != null;
  }

  @Nullable
  private RealmTweet getRealmTweet(@NonNull String idStr) {

    return realm.where(RealmTweet.class).equalTo("idStr", idStr).findFirst();
  }
}
