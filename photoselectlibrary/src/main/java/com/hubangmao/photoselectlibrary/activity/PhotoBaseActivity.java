package com.hubangmao.photoselectlibrary.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.hubangmao.photoselectlibrary.R;


public abstract class PhotoBaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initTranslucentStatusBar();
        overridePendingTransition(R.anim.photo_activity_enter, 0);
        super.onCreate(savedInstanceState);

    }

    //透明状态栏
    public void initTranslucentStatusBar() {
        //参见http://www.jianshu.com/p/bae25b5eb867 沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //查找ViewId
    public abstract void findViews();


    //加载数据
    public void initData() {
    }


    //初始化View数据
    public void initViewData() {
    }


    //初始化监听事件
    public void setListener() {
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.photo_activity_exit);
    }
}
