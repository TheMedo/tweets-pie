package com.medo.tweetspie.main.viewer;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.medo.tweetspie.database.RealmInteractor;
import com.medo.tweetspie.system.PreferencesInteractor;
import com.medo.tweetspie.utils.Constant;

import uk.co.senab.photoview.PhotoViewAttacher;


public class ViewerDialog extends DialogFragment implements ViewerContract.View {

  ImageView imageMedia;

  public static DialogFragment newInstance(@NonNull String id) {

    Bundle args = new Bundle();
    args.putString(Constant.Extras.ID, id);

    DialogFragment dialog = new ViewerDialog();
    dialog.setArguments(args);
    return dialog;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    // create the full screen image view as dialog content
    imageMedia = new ImageView(getContext());
    imageMedia.setLayoutParams(new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT));

    // create the fullscreen dialog
    final Dialog dialog = new Dialog(getActivity());
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(imageMedia);
    Window window = dialog.getWindow();
    if (window != null) {
      window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
      window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    PreferencesInteractor preferences = new PreferencesInteractor(getContext());
    RealmInteractor realmInteractor = new RealmInteractor(preferences);
    ViewerPresenter presenter = new ViewerPresenter(realmInteractor);
    presenter.onAttach(this);

    return dialog;
  }

  @Override
  public void initUi() {
    // TODO init ui
  }

  @Override
  public void showImage(@NonNull String url) {

    Glide.with(this)
            .load(url)
            .asBitmap()
            .fitCenter()
            .listener(new RequestListener<String, Bitmap>() {

              @Override
              public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {

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
            .asGif()
            .into(imageMedia);
  }

  @Override
  public void exit() {

    dismiss();
  }
}