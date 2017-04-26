package com.medo.tweetspie.bus;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;


class MainThreadBus extends Bus {

  private static Bus bus;
  private final Handler handler = new Handler(Looper.getMainLooper());

  MainThreadBus() {

    if (bus == null) {
      bus = new Bus(ThreadEnforcer.ANY);
    }
  }

  @Override
  public void register(Object obj) {

    bus.register(obj);
  }

  @Override
  public void unregister(Object obj) {

    bus.unregister(obj);
  }

  @Override
  public void post(final Object event) {

    if (Looper.myLooper() == Looper.getMainLooper()) {
      bus.post(event);
    }
    else {
      handler.post(() -> bus.post(event));
    }
  }
}
