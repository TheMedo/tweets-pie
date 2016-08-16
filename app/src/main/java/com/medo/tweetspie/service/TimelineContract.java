package com.medo.tweetspie.service;


import android.support.annotation.NonNull;

import com.medo.tweetspie.base.BaseService;
import com.medo.tweetspie.base.BaseServicePresenter;


public interface TimelineContract {

  interface Service extends BaseService {

    void exitWithSuccess();

    void exitWithError(@NonNull Exception e);
  }


  interface Presenter extends BaseServicePresenter<Service> {

  }
}
