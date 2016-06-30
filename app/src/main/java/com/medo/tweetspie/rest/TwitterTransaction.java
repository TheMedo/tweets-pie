package com.medo.tweetspie.rest;


import android.support.annotation.NonNull;


public interface TwitterTransaction {

  boolean hasActiveSession();

  void loginGuest(@NonNull LoginCallback callback);

  void getTimeline(@NonNull TweetsCallback callback);
}
