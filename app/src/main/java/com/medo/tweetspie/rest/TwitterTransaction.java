package com.medo.tweetspie.rest;


import android.support.annotation.NonNull;


public interface TwitterTransaction {

  boolean checkSession();

  void getTimeline(@NonNull TweetsCallback callback);
}
