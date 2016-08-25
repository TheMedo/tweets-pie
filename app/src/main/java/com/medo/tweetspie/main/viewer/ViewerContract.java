package com.medo.tweetspie.main.viewer;


import android.os.Bundle;
import android.support.annotation.NonNull;

import com.medo.tweetspie.base.BaseView;
import com.medo.tweetspie.base.BaseViewPresenter;


public interface ViewerContract {

  interface View extends BaseView {

    Bundle getArguments();

    void showImage(@NonNull String url);

    void showGif(@NonNull String url);

    void exit();
  }


  interface Presenter extends BaseViewPresenter<View> {

  }
}
