package com.medo.tweetspie.main.viewer;


import android.os.Bundle;
import android.text.TextUtils;

import com.medo.tweetspie.base.AbsViewPresenter;
import com.medo.tweetspie.database.RealmInteractor;
import com.medo.tweetspie.database.model.RealmTweet;
import com.medo.tweetspie.database.model.RealmTweetEntity;
import com.medo.tweetspie.utils.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.RealmList;


public class ViewerPresenter extends AbsViewPresenter<ViewerContract.View>
        implements ViewerContract.Presenter {

  private final RealmInteractor realmInteractor;

  public ViewerPresenter(RealmInteractor realmInteractor) {

    this.realmInteractor = realmInteractor;
  }

  @Override
  public void onAttach(ViewerContract.View view) {

    super.onAttach(view);

    // get the arguments and exit if invalid
    final Bundle args = getView().getArguments();
    if (args == null || args.isEmpty()) {
      getView().exit();
      return;
    }

    // get the tweetId and exit if empty or null
    final String id = args.getString(Constant.Extras.ID, null);
    if (TextUtils.isEmpty(id)) {
      getView().exit();
      return;
    }

    // get the tweetId and exit if null
    final RealmTweet tweet = realmInteractor.getTweet(id);
    if (tweet == null) {
      getView().exit();
      return;
    }

    // get the tweet media and exist if null
    final RealmList<RealmTweetEntity> entities = tweet.getExtendedEntities();
    if (entities == null || entities.isEmpty()) {
      getView().exit();
      return;
    }

    // init the ui
    getView().initUi();

    // show me media contained in this tweet
    if (entities.size() == 1) {
      // we have a single entry, show the content based on the type
      final RealmTweetEntity entity = entities.get(0);
      //noinspection ResourceType
      switch (entity.getType()) {
        case Constant.MediaType.PHOTO:
          getView().showImages(Arrays.asList(entity.getMediaUrl()));
          break;
        case Constant.MediaType.ANIMATED_GIF:
        case Constant.MediaType.VIDEO:
          getView().showVideo(entity.getVideoUrl());
          break;
      }
    }
    else {
      // we have multiple entries (only images), show all of them
      final List<String> urls = new ArrayList<>(entities.size());
      for (RealmTweetEntity entity : entities) {
        urls.add(entity.getMediaUrl());
      }
      getView().showImages(urls);
    }
  }
}
