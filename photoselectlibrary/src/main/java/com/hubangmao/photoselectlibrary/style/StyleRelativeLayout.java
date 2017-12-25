package com.hubangmao.photoselectlibrary.style;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 介绍: 使用该容器可更改主题
 * author:胡邦茂
 * CreateDate:2017年12月25日 14:35
 */
public class StyleRelativeLayout extends RelativeLayout {
    StyleManager mStyleManager;

    public StyleRelativeLayout(Context context) {
        super(context);
        mStyleManager = StyleManager.getStyleManager();
    }

    public StyleRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StyleRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(mStyleManager.getStyleBean().getStyleBackgroundColor());
    }
}
