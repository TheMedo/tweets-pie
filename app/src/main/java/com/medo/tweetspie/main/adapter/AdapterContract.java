package com.medo.tweetspie.main.adapter;


import android.support.annotation.NonNull;


public interface AdapterContract {

  interface View {

    void openTweet(@NonNull String url);

    void openUser();

    void openMedia();

    void toggleRetweet(@NonNull String id);

    void toggleFavorite(@NonNull String id);
  }


  interface Actions {

    void onDateClick(@NonNull String id, @NonNull String screenName);

    void onAvatarClick();

    void onMediaClick();

    void onRetweetClick(@NonNull String id);

    void onFavoriteClick(@NonNull String id);
  }
}
