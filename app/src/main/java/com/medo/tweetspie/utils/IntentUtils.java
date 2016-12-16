package com.medo.tweetspie.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;


public class IntentUtils {

  public static void openUrl(@NonNull Context context, @NonNull String url) {

    final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    context.startActivity(intent);
  }
}
