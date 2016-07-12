package com.medo.tweetspie.database.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class RealmTweetEntity extends RealmObject {

  @PrimaryKey
  private String mediaUrl;
  private String type;

  public String getMediaUrl() {

    return mediaUrl;
  }

  public void setMediaUrl(String mediaUrl) {

    this.mediaUrl = mediaUrl;
  }

  public String getType() {

    return type;
  }

  public void setType(String type) {

    this.type = type;
  }
}
