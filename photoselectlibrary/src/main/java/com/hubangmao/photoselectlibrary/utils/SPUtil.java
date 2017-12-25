package com.hubangmao.photoselectlibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.hubangmao.photoselectlibrary.activity.PhotoApplication;
import com.hubangmao.photoselectlibrary.style.StyleBean;


/**
 * Created by Lcc on 2017/5/3 0003.
 */

public class SPUtil {
    private static final String STYLE_MANAGER = "appStyle";

    private Context context;
    private static SPUtil instance;
    private SharedPreferences styleSP;

    private SPUtil() {
        context = PhotoApplication.getInstance().getApplicationContext();
    }

    private SharedPreferences getStyleSP() {
        if (styleSP == null) {
            styleSP = context.getSharedPreferences(STYLE_MANAGER, Context.MODE_PRIVATE);
        }
        return styleSP;
    }

    public static SPUtil getInstance() {
        if (instance == null) {
            synchronized (SPUtil.class) {
                if (instance == null) {
                    instance = new SPUtil();
                }
            }
        }
        return instance;
    }

    public StyleBean getStyle() {
        Gson gson = new Gson();
        return gson.fromJson(getStyleSP().getString("style", gson.toJson(new StyleBean())), StyleBean.class);
    }

    public void setStyle(StyleBean style) {
        SharedPreferences.Editor editor = getStyleSP().edit();
        editor.putString("style", new Gson().toJson(style));
        editor.apply();
    }
}
