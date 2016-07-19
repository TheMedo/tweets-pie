package com.medo.tweetspie.utils;


public class MathUtils {

  /**
   * Normalizes the input value from a given range into the [0.0, 1.0] range
   *
   * @param valueIn the input value
   * @param baseMin the starting range min
   * @param baseMax the starting range max
   * @return the normalized number in range
   */
  public static float normalize(final long valueIn, final long baseMin, final long baseMax) {

    return (valueIn - baseMin) / (float) (baseMax - baseMin);
  }
}
