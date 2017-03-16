package com.hubangmao.photoselectlibrary.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;


public class BitmapCache {
    private final String TAG = "BitmapCache";
    //大图 缓存
    private static LruCache<String, Bitmap> mMaxCaches;

    //小图缓存
    private static LruCache<String, Bitmap> mMinCaches;
    private static BitmapCache mBitmapCache;

    public synchronized static BitmapCache getBitmapCache() {
        if (mBitmapCache == null) {
            mBitmapCache = new BitmapCache();
            if (mMinCaches == null) {
                initMinImgCaches();
            }
            if (mMaxCaches == null) {
                initMaxImgCaches();
            }

        }
        return mBitmapCache;
    }

    //小图缓存
    private static void initMinImgCaches() {
        //获取app最大的容量，单位：byte
        long maxMemory = Runtime.getRuntime().maxMemory();
        mMinCaches = new LruCache<String, Bitmap>((int) (maxMemory / 4)) {
            //计算每个图片的大小，单位：字节
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                //计算当前bitmap的字节数
                return bitmap.getHeight() * bitmap.getRowBytes();
            }
        };
    }

    //大图缓存
    private static void initMaxImgCaches() {
        //获取app最大的容量，单位：byte
        long maxMemory = Runtime.getRuntime().maxMemory();
        mMaxCaches = new LruCache<String, Bitmap>((int) (maxMemory / 4)) {
            //计算每个图片的大小，单位：字节
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                //计算当前bitmap的字节数
                return bitmap.getHeight() * bitmap.getRowBytes();
            }
        };
    }

    private BitmapCache() {

    }

    private OnMinImgLoadListener mOnMinImgLoadListener;

    //小图缓存完成后回调
    public interface OnMinImgLoadListener {
        void onMinImgLoadListener(FileBean f);
    }

    //点击查看大图时 停止加载
    public static boolean mIsStopLoadMinImg;

    //相册初始化时先将所有 小图缓存
    public synchronized void initPhotoIconCache(ArrayList<FileBean> files, OnMinImgLoadListener minImgLoadListener) {
        if (files == null || minImgLoadListener == null) {
            return;
        }
        mOnMinImgLoadListener = minImgLoadListener;

        for (int i = 0; i < files.size(); i++) {
            if (mIsStopLoadMinImg) {
                break;
            }

            FileBean f = files.get(i);
            //缓存不为空的话直接取出
            Bitmap bitmap = mMinCaches.get(f.getImgFile().getAbsolutePath());
            if (bitmap != null) {
                f.setBitmap(bitmap);
                mOnMinImgLoadListener.onMinImgLoadListener(f);
                continue;
            }

            bitmap = getBitmap(f.getImgFile(), 150, 150);
            if (bitmap != null) {
                String key = f.getImgFile().getAbsolutePath();
                mMinCaches.put(key, bitmap);
                bitmap = mMinCaches.get(key);
            }
            f.setBitmap(bitmap);
            mOnMinImgLoadListener.onMinImgLoadListener(f);
        }
    }

    public void setMinImgBitmap(File file, ImageView imageView) {
        if (imageView == null || file == null) {
            return;
        }
        Bitmap bitmap = mMinCaches.get(file.getAbsolutePath());
        if (bitmap == null) {
            String key = file.getAbsolutePath();
            bitmap = getBitmap(file, 150, 150);
            if (bitmap == null) {
                return;
            }
            mMinCaches.put(key, bitmap);
            bitmap = mMinCaches.get(file.getAbsolutePath());
        }
        imageView.setImageBitmap(bitmap);
    }

    private Bitmap getBitmap(File file, int width, int height) {
        FileInputStream in;
        try {
            in = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] b = new byte[2048];
            int len;
            while ((len = in.read(b, 0, 2048)) != -1) {
                baos.write(b, 0, len);
            }
            baos.flush();
            byte[] bytes = baos.toByteArray();
            return getBitmap(bytes, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }
        return null;
    }

    private OnMaxImgLoadListener mOnMaxImgLoadListener;

    public interface OnMaxImgLoadListener {
        void onMaxImgLoadListener(FileBean b);
    }


    synchronized public void reduceMaxImageSize(final File file, final OnMaxImgLoadListener onMaxImgLoadListener) {
        new Thread() {
            @Override
            public void run() {
                asyMaxLoad(file, onMaxImgLoadListener);
            }
        }.start();


    }

    private void asyMaxLoad(File file, OnMaxImgLoadListener onMaxImgLoadListener) {
        if (file == null || onMaxImgLoadListener == null) {
            return;
        }
        FileBean fileBean = new FileBean();
        mOnMaxImgLoadListener = onMaxImgLoadListener;
        final Bitmap bitmap = mMaxCaches.get(file.getAbsolutePath());
        if (bitmap != null) {
            fileBean.setBitmap(bitmap);
            mOnMaxImgLoadListener.onMaxImgLoadListener(fileBean);
            return;
        }

        //所有 大图压缩 为 720*720
        Bitmap bitmap1 = getBitmap(file, 720, 720);
        if (bitmap1 != null) {
            mMaxCaches.put(file.getAbsolutePath(), bitmap1);
        }
        fileBean.setBitmap(mMaxCaches.get(file.getAbsolutePath()));
        mOnMaxImgLoadListener.onMaxImgLoadListener(fileBean);
    }


    private Bitmap getBitmap(byte[] data, int width, int height) throws OutOfMemoryError {
        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //只获取图片的宽和高
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        int scaleX = 1;
        if (width > 0 && width < options.outWidth) {
            scaleX = options.outWidth / width;
        }
        int scaleY = 1;
        if (height > 0 && height < options.outHeight) {
            scaleY = options.outHeight / height;
        }
        int scale = scaleX;
        if (scale < scaleY) {
            scale = scaleY;
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = scale;
        //使用Bitmap.Config.RGB_565比默认的Bitmap.Config.RGB_8888节省一半的内存。
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        return bitmap;
    }

    //清除缓存集合
    public static void destroy() {
        mIsStopLoadMinImg = false;
        if (mMinCaches != null) {
            mMinCaches.evictAll();
        }

        if (mMaxCaches != null) {
            mMaxCaches.evictAll();
        }

    }
}
