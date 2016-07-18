package com.medo.tweetspie.utils;


import android.support.annotation.NonNull;
import android.text.format.DateUtils;
import android.text.util.Linkify;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FormatUtils {

  private static final SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
  private static final Linkify.TransformFilter filter = new Linkify.TransformFilter() {

    public final String transformUrl(final Matcher match, String url) {

      return match.group();
    }
  };
  private static final Pattern mentionPattern = Pattern.compile("@([A-Za-z0-9_-]+)");
  private static final String mentionScheme = "http://www.twitter.com/";
  private static final Pattern hashTagPattern = Pattern.compile("#([A-Za-z0-9_-]+)");
  private static final String hashTagScheme = "http://www.twitter.com/hashtag/";
  private static final Pattern urlPattern = Pattern.compile("[a-z]+://[^ \\n]*");


  /**
   * Converts the UTC time string into millis.
   *
   * @param utcTime the UTC time in "EEE MMM dd HH:mm:ss z yyyy" format
   * @return the {@link Date} or null if the string cannot be parsed
   */
  public static Date utcToDate(@NonNull String utcTime) {

    try {
      return sdf.parse(utcTime);
    }
    catch (ParseException e) {
      return null;
    }
  }

  /**
   * Formats the UTC time string into a relative time span string.
   *
   * @param utcTime the UTC time in "EEE MMM dd HH:mm:ss z yyyy" format
   * @return the relative time string or empty if UTC time cannot be parsed
   */
  @NonNull
  public static String toRelativeDate(@NonNull String utcTime) {

    final Date createdAtDate = utcToDate(utcTime);
    if (createdAtDate == null) {
      return "";
    }
    return DateUtils.getRelativeTimeSpanString(
            createdAtDate.getTime(),
            System.currentTimeMillis(),
            TimeUnit.SECONDS.toMillis(1),
            DateUtils.FORMAT_ABBREV_RELATIVE).toString();
  }

  /**
   * Convenient method for adding clickable links to @mentions, #hashtags and urls
   * for the text set into the TextView
   *
   * @param target the {@link TextView} to {@link Linkify}
   */
  public static void addLinks(@NonNull TextView target) {

    Linkify.addLinks(target, mentionPattern, mentionScheme, null, filter);
    Linkify.addLinks(target, hashTagPattern, hashTagScheme, null, filter);
    Linkify.addLinks(target, urlPattern, null, null, filter);
  }
}
