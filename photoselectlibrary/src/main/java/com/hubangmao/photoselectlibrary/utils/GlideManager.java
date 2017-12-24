package com.hubangmao.photoselectlibrary.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hubangmao.photoselectlibrary.R;

import java.io.File;

/**
 * 介绍:  图片加载
 * author:胡邦茂
 * CreateDate: 2017/12/24 10:45
 */

public class GlideManager {
    private final static String TAG = "GlideManager";

    public static final int LOAD_ERROR_IMG = R.color.img_load_error;

    public static void loadImage(Context context, String url, int erroImg, int emptyImg, ImageView iv) {
        //原生 API
        Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).into(iv);
    }

    public static void loadImage(Context context, String url, ImageView iv) {
        Glide.with(context)
                .load(url)
                .error(LOAD_ERROR_IMG)
                .into(iv);
    }


    public static void loadGifImage(Context context, String url, ImageView iv) {
        Glide.with(context).load(url).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).error(LOAD_ERROR_IMG).into(iv);
    }

    public static void loadRoundCornerImage(Context context, String url, ImageView iv) {
        Glide.with(context)
                .load(url)
                .error(LOAD_ERROR_IMG)
                .transform(new GlideRoundTransform(context, 10))
                .into(iv);
    }


    public static void loadImage(Context context, final File file, final ImageView imageView) {
        Glide.with(context)
                .load(file)
                .into(imageView);
    }

    public static void loadImage(Context context, final int resourceId, final ImageView imageView) {
        Glide.with(context)
                .load(resourceId)
                .into(imageView);
    }


}
