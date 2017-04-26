package com.medo.tweetspie.main.viewer;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.medo.tweetspie.base.BaseView;
import com.medo.tweetspie.base.BaseViewPresenter;


public interface PhotoContract {

  interface View extends BaseView {

    void showImage(@NonNull String url);
  }


  interface Presenter extends BaseViewPresenter<View> {

    void loadImage(@Nullable String url);
  }
}
