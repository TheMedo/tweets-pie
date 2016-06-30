package com.medo.tweetspie.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.medo.tweetspie.io.rest.TwitterInteractor;
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
            new TwitterInteractor(new PreferencesInteractor(this)));
    presenter.onServiceStarted();

    return super.onStartCommand(intent, flags, startId);
  }

  @Override
  public void notifyStart() {
    // TODO send broadcast for service start
  }

  @Override
  public void exitWithSuccess() {

    Timber.v("Timeline Service Success");
    // TODO send broadcast for service end
    stopSelf();
  }

  @Override
  public void exitWithError(@NonNull Exception e) {

    Timber.e(e, "Timeline Service Error");
    // TODO send broadcast for service end
    stopSelf();
  }
}
