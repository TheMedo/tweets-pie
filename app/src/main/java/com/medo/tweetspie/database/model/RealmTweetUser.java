package com.medo.tweetspie.database.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class RealmTweetUser extends RealmObject {

  private long id;
  private int followersCount;
  private boolean friend;
  @PrimaryKey
  private String name;
  private String profileBackgroundColor;
  private String profileImageUrl;
  private boolean protectedUser;
  private String screenName;
  private boolean locked;

  public long getId() {

    return id;
  }

  public void setId(long id) {

    this.id = id;
  }

  public int getFollowersCount() {

    return followersCount;
  }

  public void setFollowersCount(int followersCount) {

    this.followersCount = followersCount;
  }

  public boolean isFriend() {

    return friend;
  }

  public void setFriend(boolean friend) {

    this.friend = friend;
  }

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

  public boolean isLocked() {

    return locked;
  }

  public void setLocked(boolean locked) {

    this.locked = locked;
  }
}
