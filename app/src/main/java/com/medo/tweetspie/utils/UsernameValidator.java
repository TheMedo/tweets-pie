package com.medo.tweetspie.utils;

import android.support.annotation.NonNull;

import java.util.regex.Pattern;


public class UsernameValidator {

  private static final String USERNAME_PATTERN = "^[A-Za-z0-9_]{1,15}$";
  private static Pattern pattern = Pattern.compile(USERNAME_PATTERN);

  public static boolean validate(@NonNull final String password) {

    return pattern.matcher(password).matches();
  }
}