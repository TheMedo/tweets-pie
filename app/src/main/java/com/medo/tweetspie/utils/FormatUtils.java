package com.medo.tweetspie.utils;


import android.support.annotation.NonNull;
import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class FormatUtils {

  private static final SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);

  /**
   * Formats the UTC time string into a relative time span string.
   *
   * @param utcTime the UTC time in "EEE MMM dd HH:mm:ss z yyyy" format
   * @return the relative time string or empty if UTC time cannot be parsed
   */
  @NonNull
  public static String toRelativeDate(@NonNull String utcTime) {

    Date date;
    try {
      date = sdf.parse(utcTime);
    }
    catch (ParseException e) {
      return "";
    }
    return DateUtils.getRelativeTimeSpanString(
            date.getTime(),
            System.currentTimeMillis(),
            TimeUnit.SECONDS.toMillis(1),
            DateUtils.FORMAT_ABBREV_RELATIVE).toString();
  }
}
