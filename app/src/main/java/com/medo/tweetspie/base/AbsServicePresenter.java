package com.medo.tweetspie.base;

import android.support.annotation.Nullable;


public abstract class AbsServicePresenter <T extends BaseService> implements BaseServicePresenter<T> {

  @Nullable
  private T service;

  @Override
  public void onStart(T service) {

    this.service = service;
  }

  @Override
  public void onStop() {

    service = null;
  }

  protected boolean isStarted() {

    return service != null;
  }

  protected T getService() {

    return service;
  }
}