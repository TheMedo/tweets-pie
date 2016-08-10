package com.medo.tweetspie.base;


import android.support.v7.app.AppCompatActivity;

import com.medo.tweetspie.bus.BusProvider;


public class BaseActivity extends AppCompatActivity {

  @Override
  protected void onResume() {

    super.onResume();
    // register the bus
    BusProvider.getInstance().register(this);
  }

  @Override
  protected void onPause() {

    super.onPause();
    // unregister the bus
    BusProvider.getInstance().unregister(this);
  }
}
