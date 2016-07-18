package com.medo.tweetspie;

import android.app.Application;

import com.medo.tweetspie.database.RealmInteractor;
import com.medo.tweetspie.rest.TwitterInteractor;
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
    TwitterInteractor.init(this);
    RealmInteractor.init(this);
  }
}
