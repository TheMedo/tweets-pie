package com.medo.tweetspie.main.viewer;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.medo.tweetspie.R;
import com.medo.tweetspie.base.BaseFragment;
import com.medo.tweetspie.injection.components.AppComponent;
import com.medo.tweetspie.injection.components.DaggerUserComponent;
import com.medo.tweetspie.utils.Constant;

import javax.inject.Inject;

import uk.co.senab.photoview.PhotoViewAttacher;


public class PhotoFragment extends BaseFragment implements PhotoContract.View {

  @Inject
  PhotoPresenter presenter;
  private ImageView imageMedia;

  public static PhotoFragment newInstance(@NonNull String type, @NonNull String url) {

    final Bundle args = new Bundle();
    args.putString(Constant.Extras.TYPE, type);
    args.putString(Constant.Extras.URL, url);

    final PhotoFragment fragment = new PhotoFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    // create the full screen image view as fragment content
    imageMedia = new ImageView(getContext());
    imageMedia.setLayoutParams(new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT));
    return imageMedia;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    super.onViewCreated(view, savedInstanceState);
    presenter.onAttach(this);
  }

  @Override
  public void onDetach() {

    super.onDetach();
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
    // no-op
  }

  @Override
  public void exit() {
    // no-op
  }

  @Override
  public void showImage(@NonNull String url) {

    Glide.with(this)
            .load(url)
            .asBitmap()
            .fitCenter()
            .listener(new RequestListener<String, Bitmap>() {

              @Override
              public boolean onException(Exception e, String model, Target<Bitmap> target,
                                         boolean isFirstResource) {

                return false;
              }

              @Override
              public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target,
                                             boolean isFromMemoryCache, boolean isFirstResource) {
                // attach the photo view for zooming / panning
                new PhotoViewAttacher(imageMedia);
                return false;
              }
            })
            .into(imageMedia);
  }

  @Override
  public void showGif(@NonNull String url) {

    Glide.with(this)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
            .placeholder(R.drawable.ic_gif)
            .into(imageMedia);
  }
}
