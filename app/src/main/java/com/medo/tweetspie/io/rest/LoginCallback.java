package com.medo.tweetspie.io.rest;


import android.support.annotation.NonNull;


public interface LoginCallback {

  void onSuccess();

  void onError(@NonNull Exception e);
}
