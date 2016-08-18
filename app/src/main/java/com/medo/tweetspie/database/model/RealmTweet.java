package com.medo.tweetspie.database.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class RealmTweet extends RealmObject {

  private String countryCode;
  private Date createdAt;
  private RealmList<RealmTweetEntity> entities;
  private RealmList<RealmTweetEntity> extendedEntities;
  private int favoriteCount;
  private boolean favorited;
  @PrimaryKey
  private String idStr;
  private int retweetCount;
  private boolean retweeted;
  private int score;
  private String text;
  private RealmTweetUser user;
  private RealmTweetUser retweetedBy;

  public String getCountryCode() {

    return countryCode;
  }

  public void setCountryCode(String countryCode) {

    this.countryCode = countryCode;
  }

  public Date getCreatedAt() {

    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {

    this.createdAt = createdAt;
  }

  public RealmList<RealmTweetEntity> getEntities() {

    return entities;
  }

  public void setEntities(RealmList<RealmTweetEntity> entities) {

    this.entities = entities;
  }

  public RealmList<RealmTweetEntity> getExtendedEntities() {

    return extendedEntities;
  }

  public void setExtendedEntities(RealmList<RealmTweetEntity> extendedEntities) {

    this.extendedEntities = extendedEntities;
  }

  public int getFavoriteCount() {

    return favoriteCount;
  }

  public void setFavoriteCount(int favoriteCount) {

    this.favoriteCount = favoriteCount;
  }

  public boolean isFavorited() {

    return favorited;
  }

  public void setFavorited(boolean favorited) {

    this.favorited = favorited;
  }

  public String getIdStr() {

    return idStr;
  }

  public void setIdStr(String idStr) {

    this.idStr = idStr;
  }

  public int getRetweetCount() {

    return retweetCount;
  }

  public void setRetweetCount(int retweetCount) {

    this.retweetCount = retweetCount;
  }

  public boolean isRetweeted() {

    return retweeted;
  }

  public void setRetweeted(boolean retweeted) {

    this.retweeted = retweeted;
  }

  public int getScore() {

    return score;
  }

  public void setScore(int score) {

    this.score = score;
  }

  public String getText() {

    return text;
  }

  public void setText(String text) {

    this.text = text;
  }

  public RealmTweetUser getUser() {

    return user;
  }

  public void setUser(RealmTweetUser user) {

    this.user = user;
  }

  public RealmTweetUser getRetweetedBy() {

    return retweetedBy;
  }

  public void setRetweetedBy(RealmTweetUser retweetedBy) {

    this.retweetedBy = retweetedBy;
  }
}
