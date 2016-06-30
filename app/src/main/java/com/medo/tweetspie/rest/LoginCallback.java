package com.medo.tweetspie.rest;


import android.support.annotation.NonNull;


public interface LoginCallback {

  void onSuccess();

  void onError(@NonNull Exception e);
}
