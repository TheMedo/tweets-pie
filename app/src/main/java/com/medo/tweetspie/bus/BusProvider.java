package com.medo.tweetspie.bus;

import com.squareup.otto.Bus;


public final class BusProvider {

  private static final MainThreadBus BUS = new MainThreadBus();

  private BusProvider() {
    // No instances.
  }

  public static Bus getInstance() {

    return BUS;
  }
}
