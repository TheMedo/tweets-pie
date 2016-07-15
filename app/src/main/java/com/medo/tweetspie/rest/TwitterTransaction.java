package com.medo.tweetspie.rest;


import android.content.Context;
import android.support.annotation.NonNull;


public interface TwitterTransaction {

  void init(@NonNull Context context);

  boolean hasActiveSession();

  void loginGuest(@NonNull LoginCallback callback);

  void getTimeline(@NonNull TweetsCallback callback);
}