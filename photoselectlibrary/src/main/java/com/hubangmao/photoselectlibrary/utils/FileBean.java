package com.hubangmao.photoselectlibrary.utils;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by 胡邦茂 on 2017/2/26.
 */

public class FileBean {
    //图片名称
    private String mFileName;
    //图片路径
    private File mImgFile;
    //Bitmap
    private Bitmap mBitmap;

    public FileBean() {
    }

    public String getFileName() {
        return mFileName;
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
