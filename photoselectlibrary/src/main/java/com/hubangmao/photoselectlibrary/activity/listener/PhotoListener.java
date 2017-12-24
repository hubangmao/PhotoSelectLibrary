package com.hubangmao.photoselectlibrary.activity.listener;


import com.hubangmao.photoselectlibrary.utils.FileBean;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by 胡邦茂 on 2017/2/26.
 * 图库选择所有接口
 */

public interface PhotoListener {

    //当 一张图片被选中或未选中回调
    interface OnAImgSelectStateListener {
        /**
         * @param file        选中图片路径
         * @param imgIsSelect 选中状态
         */
        void onAImgSelectStateListener(File file, boolean imgIsSelect);
    }

    //当点击完成回调
    interface OnBtnCompleteListener {
        void onBtnCompleteListener();
    }

    //当图片选择完成回调
    interface OnImgSelectOkListener {
        /**
         * @param selectImagePathLists 已选择图片集合路径
         */
        void onImgSelectOkListener(Set<File> selectImagePathLists);
    }

    //相册Item点击
    interface OnPhotoItemClickListener {
        void onPhotoItemClickListener(ArrayList<FileBean> photoImagePathList, String photoName);
    }

    //权限状态
    interface OnCheckPermissionStateListener {
        //用于得到权限后 刷新数据
        void onCheckPermissionStateListener();
    }

}
