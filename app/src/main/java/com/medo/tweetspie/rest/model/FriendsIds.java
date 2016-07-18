package com.medo.tweetspie.rest.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;


public class FriendsIds {

  @SerializedName("previous_cursor")
  private long previousCursor;
  @SerializedName("ids")
  private List<Long> ids;
  @SerializedName("next_cursor")
  private long nextCursor;

  public long getPreviousCursor() {

    return previousCursor;
  }

  public void setPreviousCursor(long previousCursor) {

    this.previousCursor = previousCursor;
  }

  public List<Long> getIds() {

    return ids;
  }

  public void setIds(List<Long> ids) {

    this.ids = ids;
  }

  public long getNextCursor() {

    return nextCursor;
  }

  public void setNextCursor(long nextCursor) {

    this.nextCursor = nextCursor;
  }
}
