package com.medo.tweetspie.utils;


public class MathUtils {

  /**
   * Normalizes the input value from a given range into a new range
   *
   * @param valueIn  the input value
   * @param baseMin  the starting range min
   * @param baseMax  the starting range max
   * @param limitMin the resulting range min
   * @param limitMax the resulting range max
   * @return the normalized number in range
   */
  public static long scaleInRange(final long valueIn,
                                  final long baseMin, final long baseMax,
                                  final long limitMin, final long limitMax) {

    return ((limitMax - limitMin) * (valueIn - baseMin) / (baseMax - baseMin)) + limitMin;
  }
}
