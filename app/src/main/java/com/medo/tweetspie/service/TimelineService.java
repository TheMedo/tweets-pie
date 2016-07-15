package com.medo.tweetspie.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.medo.tweetspie.bus.BusProvider;
import com.medo.tweetspie.bus.events.TimelineServiceEvent;
import com.medo.tweetspie.database.RealmInteractor;
import com.medo.tweetspie.rest.TwitterInteractor;
import com.medo.tweetspie.system.PreferencesInteractor;

import timber.log.Timber;


public class TimelineService extends Service implements TimelineContract.Service {

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

    TimelineContract.Actions presenter = new TimelinePresenter(
            this,
            new TwitterInteractor(new PreferencesInteractor(this)),
            new RealmInteractor());
    presenter.onServiceStarted();

    return super.onStartCommand(intent, flags, startId);
  }

  @Override
  public void notifyStart() {
    // TODO notify the start of this service
    Timber.v("Timeline Service Started");
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
