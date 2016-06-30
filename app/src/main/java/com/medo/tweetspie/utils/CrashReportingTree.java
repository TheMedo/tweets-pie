package com.medo.tweetspie.utils;

import android.util.Log;

import timber.log.Timber;


/**
 * A tree which logs important information for crash reporting.
 */
public class CrashReportingTree extends Timber.Tree {

  @Override
  protected void log(int priority, String tag, String message, Throwable t) {

    if (priority == Log.VERBOSE || priority == Log.DEBUG) {
      // ignore debug tags
      return;
    }

    //    FakeCrashLibrary.log(priority, tag, message);
    //
    //    if (t != null) {
    //      if (priority == Log.ERROR) {
    //        FakeCrashLibrary.logError(t);
    //      }
    //      else if (priority == Log.WARN) {
    //        FakeCrashLibrary.logWarning(t);
    //      }
    //    }
  }
}