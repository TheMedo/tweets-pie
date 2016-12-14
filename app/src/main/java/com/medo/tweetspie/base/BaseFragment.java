package com.medo.tweetspie.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.medo.tweetspie.MainApp;
import com.medo.tweetspie.bus.BusProvider;
import com.medo.tweetspie.injection.components.AppComponent;


public abstract class BaseFragment extends Fragment {

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    inject(MainApp.getAppComponent());
  }

  @Override
  public void onResume() {

    super.onResume();
    // register the bus
    BusProvider.getInstance().register(this);
  }

  @Override
  public void onPause() {

    super.onPause();
    // unregister the bus
    BusProvider.getInstance().unregister(this);
  }

  protected abstract void inject(@NonNull AppComponent appComponent);
}
