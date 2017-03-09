package com.hubangmao.photoselectlibrary.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by hbm on 2016/11/16 0016.
 */

public class Utils {
    private final String TAG = "Utils";
    private static Toast mShortToast;
    private static Utils mUtils;

    private Utils() {
    }

    public static Utils getUtils() {
        if (mUtils == null) {
            mUtils = new Utils();
        }
        return mUtils;
    }

    public void toast(Context context, String string) {
        if (context == null || string == null) {
            return;
        }
        if (mShortToast == null) {
            mShortToast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
        } else {
            mShortToast.setDuration(Toast.LENGTH_SHORT);
            mShortToast.setText(string);
        }
        mShortToast.show();
    }


}
