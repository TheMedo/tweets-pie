package com.medo.tweetspie.main.adapter;


public interface AdapterContract {

  interface View {

    void openTweet();

    void openUser();

    void openMedia();
  }


  interface Actions {

    void onDateClick();

    void onAvatarClick();

    void onMediaClick();
  }
}
