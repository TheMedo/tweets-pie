package com.medo.tweetspie.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.medo.tweetspie.MainApp;
import com.medo.tweetspie.bus.BusProvider;
import com.medo.tweetspie.bus.events.TimelineServiceEvent;
import com.medo.tweetspie.database.RealmModule;
import com.medo.tweetspie.injection.components.AppComponent;
import com.medo.tweetspie.injection.components.DaggerUserComponent;
import com.medo.tweetspie.main.MainModule;
import com.medo.tweetspie.rest.TwitterModule;

import javax.inject.Inject;

import timber.log.Timber;


public class TimelineService extends Service implements TimelineContract.Service {

  @Inject
  TimelinePresenter presenter;

  public static void start(@NonNull Context context) {

    context.startService(new Intent(context, TimelineService.class));
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {

    return null;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {

    inject(MainApp.getAppComponent());

    presenter.onStart(this);

    return super.onStartCommand(intent, flags, startId);
  }

  private void inject(@NonNull AppComponent appComponent) {

    DaggerUserComponent.builder()
            .appComponent(appComponent)
            .mainModule(new MainModule())
            .realmModule(new RealmModule())
            .twitterModule(new TwitterModule())
            .build()
            .inject(this);
  }

  @Override
  public void onDestroy() {

    super.onDestroy();
    presenter.onStop();
  }

  @Override
  public void exitWithSuccess() {
    // notify the end of this service with success
    Timber.v("Timeline Service Success");
    BusProvider.getInstance().post(new TimelineServiceEvent(true));
    stopSelf();
  }

  @Override
  public void exitWithError(@NonNull Exception e) {
    // notify the end of this service with error
    Timber.e(e, "Timeline Service Error");
    BusProvider.getInstance().post(new TimelineServiceEvent(false));
    stopSelf();
  }
}
