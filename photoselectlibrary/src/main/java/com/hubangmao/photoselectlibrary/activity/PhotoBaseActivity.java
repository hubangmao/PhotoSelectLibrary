package com.hubangmao.photoselectlibrary.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hubangmao.photoselectlibrary.R;
import com.hubangmao.photoselectlibrary.bean.ActionBarBean;


public abstract class PhotoBaseActivity extends AppCompatActivity implements BaseView {
    private boolean mIsOpenDefToolBar = true;//是否开启默认工具栏

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initTranslucentStatusBar();
        super.onCreate(savedInstanceState);

    }

    public void initTranslucentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            //状态栏颜色
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        setActionBarContainer(layoutResID);
    }

    public void setOpenDefToolBar(boolean openDefToolBar) {
        mIsOpenDefToolBar = openDefToolBar;
    }

    private void setActionBarContainer(int layoutResID) {
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ll.setOrientation(LinearLayout.VERTICAL);

        if (mIsOpenDefToolBar) {
            View toolBarView = View.inflate(this, R.layout.tool_bar_layout, null);
            ll.addView(toolBarView);

            RelativeLayout rlToolBarContent = (RelativeLayout) toolBarView.findViewById(R.id.rl_tool_bar_content);
            ImageView rvToolBarBack = (ImageView) toolBarView.findViewById(R.id.iv_tool_bar_back);
            TextView tvToolBarTitle = (TextView) toolBarView.findViewById(R.id.tv_tool_bar_title);
            TextView tvToolBarRightTitle = (TextView) toolBarView.findViewById(R.id.tv_tool_bar_title_right);
            ImageView ivToolBarRight = (ImageView) toolBarView.findViewById(R.id.iv_tool_bar_right_ic);
            rvToolBarBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            initToolBar(new ActionBarBean(rlToolBarContent, rvToolBarBack, tvToolBarTitle, tvToolBarRightTitle, ivToolBarRight));
        }


        ll.addView(View.inflate(this, layoutResID, null));
        super.setContentView(ll);

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
}
