package com.medo.tweetspie.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.medo.tweetspie.R;
import com.medo.tweetspie.database.model.RealmTweetUser;


public class ImageUtils {

  /**
   * Loads the tweet author's avatar into an image view using Glide.
   * Uses the profile background color as a fallback / placeholder.
   *
   * @param context the context for instantiating Glide
   * @param target  the ImageView to load the avatar into
   * @param user    the tweet author containing the profile data
   */
  public static void loadUserAvatar(@NonNull Context context, @NonNull ImageView target, @NonNull RealmTweetUser user) {

    int color;
    try {
      // try parsing the profile background color
      color = Color.parseColor(user.getProfileBackgroundColor());
    }
    catch (Exception e) {
      // use the accent color as a fallback
      color = ContextCompat.getColor(context, R.color.colorAccent);
    }
    final ColorDrawable placeholderDrawable = new ColorDrawable(color);
    final String avatarUrl = user.getProfileImageUrl();

    Glide.with(context)
            .load(avatarUrl)
            .asBitmap()
            .centerCrop()
            .placeholder(placeholderDrawable)
            .fallback(placeholderDrawable)
            .error(placeholderDrawable)
            .transform(new CropCircleTransformation(context))
            .into(target);
  }

  private static class CropCircleTransformation implements Transformation<Bitmap> {

    private BitmapPool bitmapPool;

    /* package */ CropCircleTransformation(Context context) {

      this(Glide.get(context).getBitmapPool());
    }

    /* package */ CropCircleTransformation(BitmapPool pool) {

      this.bitmapPool = pool;
    }

    @Override
    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {

      Bitmap source = resource.get();
      int size = Math.min(source.getWidth(), source.getHeight());

      int width = (source.getWidth() - size) / 2;
      int height = (source.getHeight() - size) / 2;

      Bitmap bitmap = bitmapPool.get(size, size, Bitmap.Config.ARGB_8888);
      if (bitmap == null) {
        bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
      }

      Canvas canvas = new Canvas(bitmap);
      Paint paint = new Paint();
      BitmapShader shader = new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
      if (width != 0 || height != 0) {
        // source isn't square, move viewport to center
        Matrix matrix = new Matrix();
        matrix.setTranslate(-width, -height);
        shader.setLocalMatrix(matrix);
      }
      paint.setShader(shader);
      paint.setAntiAlias(true);

      float r = size / 2f;
      canvas.drawCircle(r, r, r, paint);

      return BitmapResource.obtain(bitmap, bitmapPool);
    }

    @Override
    public String getId() {

      return "CropCircleTransformation()";
    }
  }
}
