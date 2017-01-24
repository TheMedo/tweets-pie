package com.medo.tweetspie.main.viewer;

import android.media.MediaPlayer;

import com.medo.tweetspie.database.RealmInteractor;
import com.medo.tweetspie.injection.scopes.UserScope;

import dagger.Module;
import dagger.Provides;


@Module
public class ViewerModule {

  @Provides
  @UserScope
  ViewerPresenter provideViewerPresenter(RealmInteractor realm) {

    return new ViewerPresenter(realm);
  }

  @Provides
  @UserScope
  PhotoPresenter providePhotoPresenter() {

    return new PhotoPresenter();
  }

  @Provides
  @UserScope
  MediaPlayer provideMediaPlayer() {

    return new MediaPlayer();
  }
}
