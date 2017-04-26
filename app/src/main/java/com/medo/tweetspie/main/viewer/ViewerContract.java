package com.medo.tweetspie.main.viewer;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.medo.tweetspie.base.BaseView;
import com.medo.tweetspie.base.BaseViewPresenter;

import java.util.List;


interface ViewerContract {

  interface View extends BaseView {

    void showImages(@NonNull List<String> urls);

    void showVideo(@NonNull String url);

    void showGif(@NonNull String url);

    void exit();
  }


  interface Presenter extends BaseViewPresenter<View> {

    void handleMedia(@Nullable String tweetId);
  }
}
