package com.medo.tweetspie.main.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.medo.tweetspie.main.viewer.PhotoFragment;

import java.util.Arrays;
import java.util.List;


public class PhotoPagerAdapter extends FragmentStatePagerAdapter {

  private final String type;
  private final List<String> urls;

  public PhotoPagerAdapter(@NonNull FragmentManager fm, @NonNull String type,
                           @NonNull String url) {

    this(fm, type, Arrays.asList(url));
  }

  public PhotoPagerAdapter(@NonNull FragmentManager fm, @NonNull String type,
                           @NonNull List<String> urls) {

    super(fm);
    this.type = type;
    this.urls = urls;
  }

  @Override
  public Fragment getItem(int position) {

    return PhotoFragment.newInstance(type, urls.get(position));
  }

  @Override
  public int getCount() {

    return urls.size();
  }
}