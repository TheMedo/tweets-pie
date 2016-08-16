package com.medo.tweetspie.rest;


import android.support.annotation.NonNull;


public interface TwitterCallback {

  void onSuccess();

  void onError(@NonNull Exception e);
}
