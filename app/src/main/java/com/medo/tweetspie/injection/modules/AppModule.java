package com.medo.tweetspie.injection.modules;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class AppModule {

  @NonNull
  private final Application application;

  public AppModule(@NonNull Application application) {

    this.application = application;
  }

  @Provides
  @Singleton
  Application provideApp() {

    return application;
  }

  @Provides
  Context provideAppContext() {

    return application.getApplicationContext();
  }
}