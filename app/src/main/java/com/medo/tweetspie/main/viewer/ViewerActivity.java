package com.medo.tweetspie.main.viewer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.medo.tweetspie.R;
import com.medo.tweetspie.base.BaseActivity;
import com.medo.tweetspie.injection.components.AppComponent;
import com.medo.tweetspie.injection.components.DaggerUserComponent;
import com.medo.tweetspie.main.adapter.PhotoPagerAdapter;
import com.medo.tweetspie.utils.Constant;
import com.medo.tweetspie.utils.ImageUtils;
import com.medo.tweetspie.utils.IntentUtils;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;


public class ViewerActivity extends BaseActivity
        implements ViewerContract.View, TextureView.SurfaceTextureListener {

  @BindView(R.id.video_view)
  VideoView videoView;
  @BindView(R.id.texture_view)
  TextureView textureView;
  @BindView(R.id.pager)
  ViewPager pager;
  @BindView(R.id.indicator)
  CircleIndicator indicator;

  @Inject
  ViewerPresenter presenter;
  @Inject
  MediaPlayer mediaPlayer;

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
    presenter.handleMedia(getIntent().getStringExtra(Constant.Extras.ID));
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

    textureView.setSurfaceTextureListener(this);
  }

  @Override
  public void exit() {

    finish();
  }

  @Override
  public void showImages(@NonNull List<String> urls) {

    final PhotoPagerAdapter adapter = new PhotoPagerAdapter(getSupportFragmentManager(), urls);
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

    try {
      mediaPlayer.setDataSource(url);
    }
    catch (IOException e) {
      // cannot play the video without a valid source
      return;
    }
    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

      @Override
      public void onPrepared(MediaPlayer mediaPlayer) {
        // adjust the ratio of the texture view to avoid stretching
        ImageUtils.adjustAspectRatio(textureView, mediaPlayer.getVideoWidth(), mediaPlayer.getVideoHeight());
        // set the gif as looping and start the playback
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
      }
    });
    // prepare the playback
    mediaPlayer.prepareAsync();
  }

  @Override
  public void showVideo(@NonNull final String url) {
    // toggling the background color is a workaround for preventing
    // view flicker when the video is initially loaded
    videoView.setBackgroundColor(Color.BLACK);
    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

      @Override
      public void onPrepared(MediaPlayer mediaPlayer) {

        videoView.setBackgroundColor(Color.TRANSPARENT);
        // create a media controller for video playback anchored to the bottom of the video
        final MediaController controller = new MediaController(ViewerActivity.this);
        videoView.setMediaController(controller);
        controller.setAnchorView(videoView);
        // start the playback
        mediaPlayer.start();
      }
    });

    videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {

      @Override
      public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        // we cannot open the video (ex. unsupported format)
        // start a view intent in case some other app can handle it
        IntentUtils.openUrl(ViewerActivity.this, url);
        exit();
        return false;
      }
    });

    // load the video and start playback immediately after
    videoView.setVideoURI(Uri.parse(url));
  }

  @Override
  public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {

    final Surface surface = new Surface(surfaceTexture);
    mediaPlayer.setSurface(surface);
  }

  @Override
  public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

  }

  @Override
  public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {

    return true;
  }

  @Override
  public void onSurfaceTextureUpdated(SurfaceTexture surface) {

  }
}
