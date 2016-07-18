package com.medo.tweetspie.database;


import android.content.Context;
import android.support.annotation.NonNull;

import com.medo.tweetspie.database.model.RealmFriendId;
import com.medo.tweetspie.database.model.RealmTweet;
import com.medo.tweetspie.database.model.RealmTweetUser;
import com.medo.tweetspie.database.utils.RealmConverter;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.ArrayList;
import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.Sort;
import timber.log.Timber;


public class RealmInteractor implements RealmTransaction {

  private Realm realm;

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

    checkInstance();
  }

  @Override
  public void onDestroy() {
    // close the default realm instance
    if (realm != null) {
      realm.close();
      realm = null;
    }
  }

  @Override
  public void persistTweets(@NonNull List<Tweet> tweets) {

    checkInstance();
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

    checkInstance();
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

    checkInstance();
    return realm.where(RealmTweet.class).findAllSorted("score", Sort.DESCENDING);
  }

  @Override
  public boolean hasFriendsIds() {

    checkInstance();
    return realm.where(RealmFriendId.class).findFirst() != null;
  }

  private void checkInstance() {
    // get the default realm instance
    if (realm == null) {
      realm = Realm.getDefaultInstance();
    }
  }

  private boolean isFriend(long id) {

    checkInstance();
    return realm.where(RealmFriendId.class).equalTo("id", id).findFirst() != null;
  }
}
