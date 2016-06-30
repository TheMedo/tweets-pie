package com.medo.tweetspie;

import android.app.Application;

import com.medo.tweetspie.consts.Constants;
import com.medo.tweetspie.utils.CrashReportingTree;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.tweetui.TweetUi;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;


public class MainApp extends Application {

  @Override
  public void onCreate() {

    super.onCreate();
    // init timber
    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }
    else {
      Timber.plant(new CrashReportingTree());
    }
    // init twitter
    TwitterAuthConfig authConfig = new TwitterAuthConfig(Constants.TWITTER_KEY, Constants.TWITTER_SECRET);
    Fabric.with(this, new TwitterCore(authConfig), new TweetUi());

    TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {

      @Override
      public void success(Result<AppSession> result) {

      }

      @Override
      public void failure(TwitterException exception) {

      }
    });
  }
}
