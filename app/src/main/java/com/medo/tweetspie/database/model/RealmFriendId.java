package com.medo.tweetspie.database.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class RealmFriendId extends RealmObject {

  @PrimaryKey
  private long id;

  public long getId() {

    return id;
  }

  public void setId(long id) {

    this.id = id;
  }
}
