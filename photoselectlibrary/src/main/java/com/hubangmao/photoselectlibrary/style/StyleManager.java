package com.hubangmao.photoselectlibrary.style;


import com.hubangmao.photoselectlibrary.utils.SPUtil;

/**
 * 介绍: 主题管理器
 * author:胡邦茂
 * CreateDate:2017年12月25日 14:37
 */
public class StyleManager {
    private static StyleManager mStyleManager;

    private StyleManager() {

    }

    public synchronized static StyleManager getStyleManager() {
        if (mStyleManager == null) {
            mStyleManager = new StyleManager();
        }
        return mStyleManager;
    }

    public StyleBean getStyleBean() {
        return SPUtil.getInstance().getStyle();
    }

}
