package com.medo.tweetspie.database.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class RealmTweetUser extends RealmObject {

  private String name;
  private String profileBackgroundColor;
  private String profileImageUrl;
  private boolean protectedUser;
  @PrimaryKey
  private String screenName;

  public String getName() {

    return name;
  }

  public void setName(String name) {

    this.name = name;
  }

  public String getProfileBackgroundColor() {

    return profileBackgroundColor;
  }

  public void setProfileBackgroundColor(String profileBackgroundColor) {

    this.profileBackgroundColor = profileBackgroundColor;
  }

  public String getProfileImageUrl() {

    return profileImageUrl;
  }

  public void setProfileImageUrl(String profileImageUrl) {

    this.profileImageUrl = profileImageUrl;
  }

  public boolean isProtectedUser() {

    return protectedUser;
  }

  public void setProtectedUser(boolean protectedUser) {

    this.protectedUser = protectedUser;
  }

  public String getScreenName() {

    return screenName;
  }

  public void setScreenName(String screenName) {

    this.screenName = screenName;
  }
}
