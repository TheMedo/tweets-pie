package com.medo.tweetspie.database;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.medo.tweetspie.database.model.RealmFriendId;
import com.medo.tweetspie.database.model.RealmTweet;
import com.medo.tweetspie.database.model.RealmTweetUser;
import com.medo.tweetspie.database.utils.RealmConverter;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.Sort;
import timber.log.Timber;


public class RealmInteractor implements RealmTransaction {

  private Realm realm;

  public RealmInteractor() {

    this.realm = Realm.getDefaultInstance();
  }

  public static void init(@NonNull Context context) {
    // create a default realm configuration that will delete the database if migration is needed
    // this will remove the migration overhead when modifying the database schema
    RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(context)
            .deleteRealmIfMigrationNeeded()
            .build();
    Realm.setDefaultConfiguration(realmConfiguration);
  }

  @Override
  public void onInitialize() {

  }

  @Override
  public void onDestroy() {

    removeOldTweets();
    realm.close();
    realm = null;
  }

  @Override
  public void persistTweets(@NonNull List<Tweet> tweets) {
    // convert all tweets to realm objects
    List<RealmTweet> realmTweets = new ArrayList<>(tweets.size());
    for (Tweet tweet : tweets) {
      RealmTweet realmTweet;
      if (tweet.retweetedStatus == null) {
        // convert the normal tweet
        realmTweet = RealmConverter.convertTweet(tweet);
      }
      else {
        // convert the retweet
        realmTweet = RealmConverter.convertTweet(tweet.retweetedStatus);
      }
      // add meta info
      final RealmTweetUser realmTweetUser = realmTweet.getUser();
      if (realmTweetUser != null) {
        realmTweetUser.setFriend(isFriend(realmTweetUser.getId()));
      }

      realmTweets.add(realmTweet);
    }
    // persist the realm tweets
    realm.beginTransaction();
    realm.copyToRealmOrUpdate(realmTweets);
    realm.commitTransaction();

    Timber.d("Persisted %d tweets", realmTweets.size());
  }

  @Override
  public void persistFriendsIds(@NonNull List<Long> friendsIds) {
    // convert all tweets to realm objects
    List<RealmFriendId> realmFriendIds = new ArrayList<>(friendsIds.size());
    for (Long id : friendsIds) {
      realmFriendIds.add(RealmConverter.convertFriendId(id));
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

  @Override
  public boolean hasFriendsIds() {

    return realm.where(RealmFriendId.class).findFirst() != null;
  }

  @Override
  public void toggleRetweet(@NonNull String id) {

    RealmTweet tweet = getRealmTweet(id);
    if (tweet == null) {
      return;
    }
    final boolean isRetweeted = tweet.isRetweeted();
    final int retweetCount = tweet.getRetweetCount();
    realm.beginTransaction();
    tweet.setRetweeted(!isRetweeted);
    tweet.setRetweetCount(isRetweeted ? retweetCount - 1 : retweetCount + 1);
    realm.commitTransaction();
  }

  @Override
  public void toggleFavorite(@NonNull String id) {

    RealmTweet tweet = getRealmTweet(id);
    if (tweet == null) {
      return;
    }
    final boolean isFavorited = tweet.isFavorited();
    final int favoriteCount = tweet.getFavoriteCount();
    realm.beginTransaction();
    tweet.setFavorited(!isFavorited);
    tweet.setFavoriteCount(isFavorited ? favoriteCount - 1 : favoriteCount + 1);
    realm.commitTransaction();
  }

  /**
   * Deletes all tweets older than a day from the database
   */
  private void removeOldTweets() {

    Calendar yesterday = Calendar.getInstance();
    yesterday.add(Calendar.DAY_OF_YEAR, -1);

    realm.beginTransaction();
    realm.where(RealmTweet.class)
            .lessThan("createdAt", yesterday.getTime())
            .findAll()
            .deleteAllFromRealm();
    realm.commitTransaction();
  }

  private boolean isFriend(long id) {

    return realm.where(RealmFriendId.class).equalTo("id", id).findFirst() != null;
  }

  @Nullable
  private RealmTweet getRealmTweet(@NonNull String idStr) {

    return realm.where(RealmTweet.class).equalTo("idStr", idStr).findFirst();
  }
}
