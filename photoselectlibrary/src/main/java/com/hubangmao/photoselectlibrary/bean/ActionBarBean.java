package com.hubangmao.photoselectlibrary.bean;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 介绍: Action Bar View 实体
 * author:胡邦茂
 * CreateDate:2017年12月20日 10:00
 */
public class ActionBarBean {
    private RelativeLayout mRlContent;
    private ImageView mIvBack;//返回
    private TextView mTvTitle;//标题
    private TextView mTvRight;//右边标题
    private ImageView mIvRight;//右边图标


    public ActionBarBean() {

    }

    public ActionBarBean(RelativeLayout rlContent, ImageView ivBack, TextView tvTitle, TextView tvRight, ImageView ivRight) {
        mRlContent = rlContent;
        mIvBack = ivBack;
        mTvTitle = tvTitle;
        mTvRight = tvRight;
        mIvRight = ivRight;
    }

    public RelativeLayout getRlContent() {
        return mRlContent;
    }

    public void setRlContent(RelativeLayout rlContent) {
        mRlContent = rlContent;
    }

    public ImageView getIvBack() {
        return mIvBack;
    }

    public void setIvBack(ImageView ivBack) {
        mIvBack = ivBack;
    }

    public TextView getTvTitle() {
        return mTvTitle;
    }

    public void setTvTitle(TextView tvTitle) {
        mTvTitle = tvTitle;
    }

    public TextView getTvRight() {
        return mTvRight;
    }

    public void setTvRight(TextView tvRight) {
        mTvRight = tvRight;
    }

    public ImageView getIvRight() {
        return mIvRight;
    }

    public void setIvRight(ImageView ivRight) {
        mIvRight = ivRight;
    }
}
