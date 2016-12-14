package com.medo.tweetspie.main.viewer;


import android.os.Bundle;
import android.text.TextUtils;

import com.medo.tweetspie.base.AbsViewPresenter;
import com.medo.tweetspie.utils.Constant;


public class PhotoPresenter extends AbsViewPresenter<PhotoContract.View>
        implements PhotoContract.Presenter {

  @Override
  public void onAttach(PhotoContract.View view) {

    super.onAttach(view);

    // get the arguments and exit if invalid
    final Bundle args = getView().getArguments();
    if (args == null || args.isEmpty()) {
      return;
    }

    // get the tweetId and exit if empty or null
    final String url = args.getString(Constant.Extras.URL, null);
    if (TextUtils.isEmpty(url)) {
      return;
    }

    // get the media type, default to image
    final String type = args.getString(Constant.Extras.TYPE, "");
    switch (type) {
      case Constant.MediaType.ANIMATED_GIF:
        getView().showGif(url);
        break;
      default:
        getView().showImage(url);
        break;
    }
  }
}
