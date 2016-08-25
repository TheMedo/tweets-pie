package com.medo.tweetspie.main.viewer;


import android.os.Bundle;
import android.text.TextUtils;

import com.medo.tweetspie.base.AbsViewPresenter;
import com.medo.tweetspie.database.RealmInteractor;
import com.medo.tweetspie.database.model.RealmTweet;
import com.medo.tweetspie.database.model.RealmTweetEntity;
import com.medo.tweetspie.utils.Constant;

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

    // show me media contained in this tweet
    final RealmTweetEntity entity = entities.get(0);
    //noinspection ResourceType
    switch (entity.getType()) {
      case Constant.MediaType.ANIMATED_GIF:
        getView().showGif(entity.getMediaUrl());
        break;
      case Constant.MediaType.PHOTO:
        getView().showImage(entity.getMediaUrl());
        break;
    }
    // TODO handle video
  }
}
