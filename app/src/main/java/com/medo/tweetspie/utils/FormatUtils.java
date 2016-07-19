package com.medo.tweetspie.utils;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

  private static final SimpleDateFormat SDF = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
  private static final Linkify.TransformFilter FILTER = new Linkify.TransformFilter() {

    public final String transformUrl(final Matcher match, String url) {

      return match.group();
    }
  };
  private static final Pattern MENTION_PATTERN = Pattern.compile("@([A-Za-z0-9_-]+)");
  private static final String MENTION_SCHEME = "http://www.twitter.com/";
  private static final Pattern HASH_TAG_PATTERN = Pattern.compile("#([A-Za-z0-9_-]+)");
  private static final String HASH_TAG_SCHEME = "http://www.twitter.com/hashtag/";
  private static final Pattern URL_PATTERN = Pattern.compile("[a-z]+://[^ \\n]*");
  private static final char[] SUFFIXES = {'K', 'M', 'G'};


  /**
   * Converts the UTC time string into millis.
   *
   * @param utcTime the UTC time in "EEE MMM dd HH:mm:ss z yyyy" format
   * @return the {@link Date} or null if the string cannot be parsed
   */
  public static Date utcToDate(@NonNull String utcTime) {

    try {
      return SDF.parse(utcTime);
    }
    catch (ParseException e) {
      return null;
    }
  }

  /**
   * Formats the UTC time string into a relative time span string.
   *
   * @param date the date to be formatted
   * @return the relative time string or empty if UTC time cannot be parsed
   */
  @NonNull
  public static String toRelativeDate(@Nullable Date date) {

    if (date == null) {
      return "";
    }
    return DateUtils.getRelativeTimeSpanString(
            date.getTime(),
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

    Linkify.addLinks(target, MENTION_PATTERN, MENTION_SCHEME, null, FILTER);
    Linkify.addLinks(target, HASH_TAG_PATTERN, HASH_TAG_SCHEME, null, FILTER);
    Linkify.addLinks(target, URL_PATTERN, null, null, FILTER);
  }


  /**
   * Formats large numbers with suffix format K, M, G
   *
   * @param number the number to format
   * @return the formatted number
   */
  public static String formatNumber(long number) {

    if (number < 1000) {
      return String.valueOf(number);
    }

    final String string = String.valueOf(number);
    final int magnitude = (string.length() - 1) / 3;
    final int digits = (string.length() - 1) % 3 + 1;

    char[] value = new char[4];
    for (int i = 0; i < digits; i++) {
      value[i] = string.charAt(i);
    }
    int valueLength = digits;
    if (digits == 1 && string.charAt(1) != '0') {
      value[valueLength++] = '.';
      value[valueLength++] = string.charAt(1);
    }
    value[valueLength++] = SUFFIXES[magnitude - 1];
    return new String(value, 0, valueLength);
  }

  public static String getTweetUrl(@NonNull String id, @NonNull String screenName) {

    return String.format("http://twitter.com/%s/status/%s", screenName, id);
  }
}
