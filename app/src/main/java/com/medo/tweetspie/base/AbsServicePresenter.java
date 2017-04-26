package com.medo.tweetspie.base;

import android.support.annotation.Nullable;


public abstract class AbsServicePresenter <T extends BaseService> implements BaseServicePresenter<T> {

  @Nullable
  protected T service;

  @Override
  public void onStart(T service) {

    this.service = service;
  }

  @Override
  public void onStop() {

    service = null;
  }
}