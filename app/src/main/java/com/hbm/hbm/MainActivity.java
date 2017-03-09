package com.hbm.hbm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hubangmao.photoselectlibrary.activity.SelectImgActivity;
import com.hubangmao.photoselectlibrary.activity.listener.PhotoListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    TextView mTvPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvPath = (TextView) findViewById(R.id.tv_path);
    }

    //一个页面如果有多次选取图片 功能 需要先清除之前一次选中状态  SelectImgActivity.destroy(false);
    public void onClick(View view) {
        //第一步 图片选择成功回调
        SelectImgActivity.setOnImgSelectOkListener(new PhotoListener.OnImgSelectOkListener() {
            @Override
            public void onImgSelectOkListener(ArrayList<File> selectImagePathLists) {
                if (selectImagePathLists == null) {
                    return;
                }
                Log.i("main", TAG + "本次选择图片共" + selectImagePathLists.size() + "张");


            }
        });
        //第二步 设置最大选取图片数量 10
        SelectImgActivity.SET_SELECT_MAX_NUM = 10;
        //第三步 启动选择Activity
        startActivity(new Intent(this, SelectImgActivity.class));
        //外部 有取消选择图片功能需要调用此方法 更新选中状态
        //SelectImgActivity.setOnSelectImgDelete(new File(""));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁时需要调用销毁
        SelectImgActivity.destroy(false);

    }
}
