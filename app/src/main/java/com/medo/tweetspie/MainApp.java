package com.medo.tweetspie;

import android.app.Application;
import android.support.annotation.NonNull;

import com.medo.tweetspie.database.RealmInteractor;
import com.medo.tweetspie.injection.components.AppComponent;
import com.medo.tweetspie.injection.components.DaggerAppComponent;
import com.medo.tweetspie.injection.modules.AppModule;
import com.medo.tweetspie.rest.TwitterInteractor;
import com.medo.tweetspie.system.SystemModule;
import com.medo.tweetspie.utils.CrashReportingTree;

import timber.log.Timber;


public class MainApp extends Application {

  private static AppComponent appComponent;

  @NonNull
  public static AppComponent getAppComponent() {

    return appComponent;
  }

  @Override
  public void onCreate() {

    super.onCreate();

    // dependency injection (app scope)
    appComponent = DaggerAppComponent.builder()
            .appModule(new AppModule(this))
            .systemModule(new SystemModule())
            .build();

    // init timber
    Timber.plant(BuildConfig.DEBUG ? new Timber.DebugTree() : new CrashReportingTree());

    // init twitter
    TwitterInteractor.init(this);

    // init realm
    RealmInteractor.init(this);
  }
}
