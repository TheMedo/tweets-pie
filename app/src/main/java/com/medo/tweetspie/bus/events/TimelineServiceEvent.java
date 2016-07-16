package com.medo.tweetspie.bus.events;


public class TimelineServiceEvent {

  private final boolean success;

  public TimelineServiceEvent(boolean success) {

    this.success = success;
  }

  public boolean isSuccess() {

    return success;
  }
}
