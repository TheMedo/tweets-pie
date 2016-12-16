package com.medo.tweetspie.main.viewer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.medo.tweetspie.R;
import com.medo.tweetspie.base.BaseActivity;
import com.medo.tweetspie.injection.components.AppComponent;
import com.medo.tweetspie.injection.components.DaggerUserComponent;
import com.medo.tweetspie.main.adapter.PhotoPagerAdapter;
import com.medo.tweetspie.utils.Constant;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;


public class ViewerActivity extends BaseActivity implements ViewerContract.View {

  @BindView(R.id.pager)
  ViewPager pager;
  @BindView(R.id.indicator)
  CircleIndicator indicator;

  @Inject
  ViewerPresenter presenter;

  @NonNull
  public static Intent getIntent(@NonNull Activity parent, @NonNull String tweetId) {

    final Intent intent = new Intent(parent, ViewerActivity.class);
    intent.putExtra(Constant.Extras.ID, tweetId);
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    presenter.onAttach(this);
  }

  @Override
  protected void onDestroy() {

    super.onDestroy();
    presenter.onDetach();
  }

  @Override
  protected void inject(@NonNull AppComponent appComponent) {

    DaggerUserComponent.builder()
            .appComponent(appComponent)
            .viewerModule(new ViewerModule())
            .build()
            .inject(this);
  }

  @Override
  public void initUi() {

    final Window window = getWindow();
    window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

    setContentView(R.layout.activity_viewer);
    ButterKnife.bind(this);
  }

  @Override
  public void exit() {

    finish();
  }

  @Override
  public Bundle getArguments() {

    return getIntent().getExtras();
  }

  @Override
  public void showImages(@NonNull List<String> urls) {

    final PhotoPagerAdapter adapter
            = new PhotoPagerAdapter(getSupportFragmentManager(), Constant.MediaType.PHOTO, urls);
    pager.setAdapter(adapter);
    if (urls.size() > 1) {
      indicator.setViewPager(pager);
    }
    else {
      indicator.setVisibility(View.GONE);
    }
  }

  @Override
  public void showGif(@NonNull String url) {

    final PhotoPagerAdapter adapter
            = new PhotoPagerAdapter(getSupportFragmentManager(), Constant.MediaType.ANIMATED_GIF, url);
    pager.setAdapter(adapter);
    indicator.setVisibility(View.GONE);
  }

  @Override
  public void showVideo(@NonNull String url) {

    final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    startActivity(intent);
    finish();
  }
}
