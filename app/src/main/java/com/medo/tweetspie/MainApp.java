package com.medo.tweetspie;

import android.app.Application;

import com.medo.tweetspie.database.RealmInteractor;
import com.medo.tweetspie.rest.TwitterInteractor;
import com.medo.tweetspie.system.PreferencesInteractor;
import com.medo.tweetspie.utils.CrashReportingTree;

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
    new TwitterInteractor(new PreferencesInteractor(this)).init(this);
    new RealmInteractor().init(this);
  }
}
