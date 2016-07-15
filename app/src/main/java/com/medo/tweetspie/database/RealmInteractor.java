package com.medo.tweetspie.database;


import android.content.Context;
import android.support.annotation.NonNull;

import com.medo.tweetspie.database.model.RealmTweet;
import com.medo.tweetspie.database.utils.RealmConverter;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;


public class RealmInteractor implements RealmTransaction {

  private Realm realm;

  @Override
  public void init(@NonNull Context context) {
    // create a default realm configuration that will delete the database if migration is needed
    // this will remove the migration overhead when modifying the database schema
    RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(context)
            .deleteRealmIfMigrationNeeded()
            .build();
    Realm.setDefaultConfiguration(realmConfiguration);
  }

  @Override
  public void onInitialize() {
    // get the default realm instance
    if (realm == null) {
      realm = Realm.getDefaultInstance();
    }
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
    // obtain the default instance if needed
    if (realm == null) {
      onInitialize();
    }
    // convert all tweets to realm objects
    List<RealmTweet> realmTweets = new ArrayList<>(tweets.size());
    for (Tweet tweet : tweets) {
      realmTweets.add(RealmConverter.convert(tweet));
    }
    // persist the realm tweets
    realm.beginTransaction();
    realm.copyToRealmOrUpdate(realmTweets);
    realm.commitTransaction();

    Timber.d("Persisted %d tweets", realmTweets.size());
  }
}
