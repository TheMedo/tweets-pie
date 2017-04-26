package com.medo.tweetspie.base;

import android.support.annotation.Nullable;


public abstract class AbsViewPresenter <T extends BaseView> implements BaseViewPresenter<T> {

  @Nullable
  protected T view;

  @Override
  public void onAttach(T view) {

    this.view = view;
  }

  @Override
  public void onDetach() {

    view = null;
  }
}