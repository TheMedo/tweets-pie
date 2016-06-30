package com.medo.tweetspie.service;


import android.support.annotation.MainThread;
import android.support.annotation.NonNull;


public interface TimelineContract {

  interface Service {

    void notifyStart();

    void exitWithSuccess();

    void exitWithError(@NonNull Exception e);
  }


  interface Actions {

    @MainThread
    void onServiceStarted();
  }
}
