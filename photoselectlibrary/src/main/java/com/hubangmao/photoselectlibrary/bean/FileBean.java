package com.hubangmao.photoselectlibrary.bean;

import android.graphics.Bitmap;

import java.io.File;

/**
 * 介绍:  文件实体
 * author:胡邦茂
 * CreateDate: 2017/12/25 15:35
 */
public class FileBean {
    //图片名称
    private String mFileName;
    //图片路径
    private File mImgFile;
    //时间排序使用
    private long mImgDate;
    //Bitmap
    private Bitmap mBitmap;

    public String getFileName() {
        return mFileName;
    }

    //
    public FileBean() {
    }

    public void setFileName(String fileName) {
        mFileName = fileName;
    }

    public File getImgFile() {
        return mImgFile;
    }

    public void setImgFile(File imgFile) {
        mImgFile = imgFile;
    }

    public long getImgDate() {
        return mImgDate;
    }

    public void setImgDate(long imgDate) {
        mImgDate = imgDate;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    @Override
    public String toString() {
        return "FileBean{" +
                "mFileName='" + mFileName + '\'' +
                ", mImgFile=" + mImgFile +
                '}';
    }

}
