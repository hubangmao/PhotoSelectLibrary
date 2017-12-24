package com.hubangmao.photoselectlibrary.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.hubangmao.photoselectlibrary.R;
import com.hubangmao.photoselectlibrary.activity.listener.PhotoListener;

import java.util.ArrayList;

/**
 * 介绍: 文件读写和照相权限
 * author:胡邦茂
 * CreateDate:2017年12月23日 10:13
 */
public class CheckPermission {
    private final String TAG = "CheckPermission";
    private static final int PERMISSIONS_REQUEST_CODE = 3000;
    private Activity mActivity;
    private boolean mPermissionStatus = true;//默认没有获取到权限
    private PhotoListener.OnCheckPermissionStateListener mOnCheckPermissionStateListener;

    public CheckPermission(Activity activity, PhotoListener.OnCheckPermissionStateListener listener) {
        mActivity = activity;
        mOnCheckPermissionStateListener = listener;
        if (Build.VERSION.SDK_INT >= 23 && mActivity.getApplicationInfo().targetSdkVersion >= 23) {
            if (mPermissionStatus) {
                checkAndRequestPermissions();
            } else {
                mOnCheckPermissionStateListener.onCheckPermissionStateListener();
            }
        } else {
            mOnCheckPermissionStateListener.onCheckPermissionStateListener();
        }

    }


    //调用封装好的申请权限的方法
    private void checkAndRequestPermissions() {
        ArrayList<String> permissionList = new ArrayList<>();
        permissionList.add(Manifest.permission.CAMERA);
        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);


        for (int i = 0; i < permissionList.size(); i++) {
            String permission = permissionList.get(i);
            //检查权限是否已经申请
            int hasPermission = ContextCompat.checkSelfPermission(mActivity, permission);
            if (hasPermission == PackageManager.PERMISSION_GRANTED) {
                permissionList.remove(i);
            }
        }
        /**
         *补充说明：ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO);
         *对于原生Android，如果用户选择了“不再提示”，那么shouldShowRequestPermissionRationale就会为true。
         *此时，用户可以弹出一个对话框，向用户解释为什么需要这项权限。
         *对于一些深度定制的系统，如果用户选择了“不再提示”，那么shouldShowRequestPermissionRationale永远为false
         *
         */

        if (permissionList.size() == 0) {
            mOnCheckPermissionStateListener.onCheckPermissionStateListener();
            return;
        }
        String[] permissions = permissionList.toArray(new String[0]);
        //正式请求权限
        ActivityCompat.requestPermissions(mActivity, permissions, PERMISSIONS_REQUEST_CODE);
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PERMISSIONS_REQUEST_CODE == requestCode) {
            mPermissionStatus = verifyPermissions(grantResults);
            Log.e(TAG, "权限转态" + mPermissionStatus);
            if (!mPermissionStatus) {
                new HintDialogUtil(mActivity.getResources().getString(R.string.hint_no_read_permission))
                        .setIsClose(false)
                        .show(mActivity, new HintDialogUtil.DialogClickListener() {
                            @Override
                            public void dialogPerformBtnClickListener(Dialog dialog) {
                                startAppSettings();
                            }

                            @Override
                            public void dialogCloseBtnClickListener() {
                                Toast.makeText(mActivity, mActivity.getResources().getString(R.string.hint_no_read_permission), Toast.LENGTH_SHORT).show();
                                mActivity.finish();
                            }
                        });
            } else {
                mOnCheckPermissionStateListener.onCheckPermissionStateListener();
            }

        }

    }

    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                //未同意权限
                return false;
            }
        }
        return true;
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + mActivity.getPackageName()));
        mActivity.startActivity(intent);
    }
}