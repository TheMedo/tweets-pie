package com.medo.tweetspie.main.viewer;


import android.support.annotation.Nullable;

import com.medo.tweetspie.base.AbsViewPresenter;


public class PhotoPresenter extends AbsViewPresenter<PhotoContract.View>
        implements PhotoContract.Presenter {

  @Override
  public void loadImage(@Nullable String url) {
    // get the tweetId and exit if empty or null
    if (view != null) {
      if (url == null) {
        view.exit();
        return;
      }
      view.showImage(url);
    }
  }
}
