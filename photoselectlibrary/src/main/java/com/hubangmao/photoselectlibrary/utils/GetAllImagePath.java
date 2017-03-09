package com.hubangmao.photoselectlibrary.utils;


import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by 胡邦茂 on 2017/2/22.
 *
 * @ about 获取所以图片路径 以及图片所在 文件夹 分类出来
 */

public class GetAllImagePath {
    private final String TAG = "GetAllImagePath";
    //加载所有图片成功
    private final int LOAD_IMAGE_PATH_OK = 1;
    private Context mContext;
    //所有图片路径
    private ArrayList<FileBean> mAllImagePathList;
    //图片分类
    private HashMap<String, ArrayList<FileBean>> mPhotoPathMapSet;
    private OnSDImagePathLoadOkListener mListener;
    private Handler mHandler;

    public interface OnSDImagePathLoadOkListener {
        void onResponse(ArrayList<FileBean> allImagePathList);

        void onResponse(HashMap<String, ArrayList<FileBean>> photoPathMapSet);
    }

    public GetAllImagePath(Context context) {
        mContext = context;
        initHandler();

    }

    public void getImageAllPath(OnSDImagePathLoadOkListener listener) {
        if (mContext == null || listener == null) {
            return;
        }

        mListener = listener;
        mAllImagePathList = new ArrayList<>();
        mPhotoPathMapSet = new HashMap<>();
        //异步获取
        new Thread(new Runnable() {
            @Override
            public void run() {
                findImages();
            }
        }).start();

    }

    private void findImages() {
        Cursor cursor = mContext.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            //获取图片的名称
            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            //获取图片的生成日期
            //String date = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            //获取图片的详细信息
            //String desc = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DESCRIPTION));
            // 获取图片的路径
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            //所有图片路径
            FileBean fileBean = new FileBean();
            fileBean.setImgFile(new File(path));
            fileBean.setFileName(name);
            mAllImagePathList.add(fileBean);
        }
        mPhotoPathMapSet.put("最近图片", mAllImagePathList);

        ArrayList<FileBean> photoFileList;

        //所有图片文件夹名称
        HashSet<String> photoFileParentPathSet = new HashSet<>();

        //获取所有图片文件夹名称
        for (FileBean f : mAllImagePathList) {
            String phoneName = f.getImgFile().getParent();
            phoneName = phoneName.substring(phoneName.lastIndexOf("/") + 1, phoneName.length());
            photoFileParentPathSet.add(phoneName);
        }

        Iterator<String> iterator = photoFileParentPathSet.iterator();


        //将所以文件夹中的图片 对号入座
        while (iterator.hasNext()) {
            photoFileList = new ArrayList<>();
            String value = iterator.next();
            for (FileBean f : mAllImagePathList) {
                //相册名称
                String phoneName = f.getImgFile().getParent();
                phoneName = phoneName.substring(phoneName.lastIndexOf("/") + 1, phoneName.length());
                //所有图片的文件夹名称 与 限定文件夹相等 说明处于同文件夹下
                if (phoneName.equals(value)) {
                    FileBean fileBean = new FileBean();
                    fileBean.setImgFile(f.getImgFile());
                    fileBean.setFileName(f.getFileName());
                    photoFileList.add(fileBean);
                    mPhotoPathMapSet.put(value, photoFileList);
                }
            }

        }
        /*Log.i("main", "文件夹个数" + mPhotoPathMapSet.size());
        Log.i("main", "文件夹DCIM/图片数量" + mPhotoPathMapSet.get("DCIM"));
        Log.i("main", "文件夹Screenshots/图片数量" + mPhotoPathMapSet.get("Screenshots"));*/

        mHandler.sendEmptyMessage(LOAD_IMAGE_PATH_OK);
    }


    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case LOAD_IMAGE_PATH_OK:
                        mListener.onResponse(mAllImagePathList);
                        mListener.onResponse(mPhotoPathMapSet);
                        break;
                }
            }
        };
    }
}
