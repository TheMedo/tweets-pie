package com.medo.tweetspie.main.adapter;


import android.support.annotation.NonNull;

import com.medo.tweetspie.base.BaseView;
import com.medo.tweetspie.base.BaseViewPresenter;


public interface AdapterContract {

  interface View extends BaseView {

    void openUrl(@NonNull String url);

    void openMedia();
  }


  interface Presenter extends BaseViewPresenter<View> {

    void onDateClick(@NonNull String id, @NonNull String screenName);

    void onAvatarClick(@NonNull String screenName);

    void onMediaClick();

    void onRetweetClick(@NonNull String id);

    void onFavoriteClick(@NonNull String id);
  }
}
