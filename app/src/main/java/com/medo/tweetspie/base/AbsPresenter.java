package com.medo.tweetspie.base;

import android.support.annotation.Nullable;


public abstract class AbsPresenter <T extends BaseView> implements BasePresenter<T> {

  @Nullable
  private T view;

  @Override
  public void onAttachView(T view) {

    this.view = view;
  }

  @Override
  public void onDetachView() {

    view = null;
  }

  protected boolean isViewAttached() {

    return view != null;
  }

  protected T getView() {

    return view;
  }
}