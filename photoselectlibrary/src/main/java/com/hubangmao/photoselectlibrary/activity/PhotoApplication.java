package com.hubangmao.photoselectlibrary.activity;

import android.app.Application;
import android.content.Context;

/**
 * 介绍:
 * author:胡邦茂
 * CreateDate:2017年12月25日 15:21
 */
public class PhotoApplication extends Application {


    private static Context sInstance;

    public static Context getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
}
