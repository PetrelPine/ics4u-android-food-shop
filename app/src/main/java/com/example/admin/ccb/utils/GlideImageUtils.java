package com.example.admin.ccb.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.admin.ccb.R;

import www.ccb.com.common.utils.UiUtils;


public class GlideImageUtils {

    private static final RequestOptions options = new RequestOptions()
            .placeholder(R.mipmap.loading)
            .error(R.mipmap.emptyimage)
            .centerCrop();

    public static void display(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    public static void display(Context context, Integer src, ImageView imageView) {
        Glide.with(context)
                .load(src)
                .apply(options)
                .into(imageView);
    }

    public static void displayTransition(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .apply(options)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);
    }

    public static void DisplayHomeGridViewItem(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.mipmap.emptyimage)
                .transform(new GlideRoundTransform(context, 0, context.getResources().getColor(R.color.colorWhite))) //color_e0e0e0
                ;
        Glide.with(context).load(url)
                .apply(options)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);
    }

    public static void DisplayNoPlaceholder(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .apply(options)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);
    }

    public static void displayTransitionNoPlace(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .apply(options)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);
    }

    public static void DisplayNoPlaceholderErrorf0f0f0(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
//                .placeholder(R.color.color_f0f0f0)
//                .error(R.color.color_f0f0f0)
//                .centerCrop()
//                .crossFade()
                .apply(options)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);
    }

    public static void DisplayCircle(final Context context, Integer src, final ImageView imageView) {
        DisplayCircle(context, src, null, imageView);
    }

    public static void DisplayCircle(final Context context, String url, final ImageView imageView) {
        DisplayCircle(context, 0, url, imageView);
    }

    public static void DisplayCircle(final Context context, Integer src, String url, final ImageView imageView) {
        Glide.with(context)
                .asBitmap()
                .load(url == null ? src : url)
                .apply(options)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    public static void DisplayRoundCorner(final Context context, String url, final ImageView imageView,
                                          final int dpCorner) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(options)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        circularBitmapDrawable.setCornerRadius(UiUtils.dp2px(dpCorner));
                        circularBitmapDrawable.setAntiAlias(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    public static void DisplayRoundCorner(final Context context, Integer src, final ImageView imageView,
                                          final int dpCorner) {
        Glide.with(context)
                .asBitmap()
                .load(src)
                .apply(options)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        circularBitmapDrawable.setCornerRadius(UiUtils.dp2px(dpCorner));
                        circularBitmapDrawable.setAntiAlias(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    public static void displayGif(Context context, @DrawableRes int resId, final ImageView imageView) {
        Glide.with(context)
                .asGif()
                .apply(options)
                .load(resId)
                .into(new SimpleTarget<GifDrawable>() {
                    @Override
                    public void onResourceReady(@NonNull GifDrawable resource, @Nullable Transition<? super GifDrawable> transition) {
                        if (resource instanceof GifDrawable) {
                            GifDrawable gifDrawable = resource;
                            gifDrawable.setLoopCount(GifDrawable.LOOP_FOREVER);
                            imageView.setImageDrawable(resource);
                            gifDrawable.start();
                        }
                    }

                });
    }

    public static void display(Context context, String url, ImageView imageView, boolean noCrop) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.loading)
                .error(R.mipmap.emptyimage);

        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    public static void display(Context context, Integer src, ImageView imageView, boolean noCrop) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.loading)
                .error(R.mipmap.emptyimage);

        Glide.with(context)
                .load(src)
                .apply(options)
                .into(imageView);
    }
}
