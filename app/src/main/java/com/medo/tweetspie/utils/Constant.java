package com.medo.tweetspie.utils;


import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class Constant {

  @Retention(RetentionPolicy.SOURCE)
  @StringDef({MediaType.PHOTO, MediaType.ANIMATED_GIF, MediaType.VIDEO})
  public @interface MediaType {

    String PHOTO = "photo";
    String ANIMATED_GIF = "animated_gif";
    String VIDEO = "video";
  }


  @Retention(RetentionPolicy.SOURCE)
  @StringDef({Extras.ID})
  public @interface Extras {

    String ID = "id";
    String URL = "url";
    String TYPE = "type";
  }
}
