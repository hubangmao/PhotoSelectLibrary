package com.hubangmao.photoselectlibrary.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hubangmao.photoselectlibrary.R;
import com.hubangmao.photoselectlibrary.activity.listener.PhotoListener;
import com.hubangmao.photoselectlibrary.adapter.PhotoListDialogAdapter;
import com.hubangmao.photoselectlibrary.adapter.SelectImgAdapter;
import com.hubangmao.photoselectlibrary.utils.BitmapCache;
import com.hubangmao.photoselectlibrary.utils.FileBean;
import com.hubangmao.photoselectlibrary.utils.GetAllImagePath;
import com.hubangmao.photoselectlibrary.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by 胡邦茂 on 2017/2/22.
 *
 * @ about 查看或选择 全部图片
 */
public class SelectImgActivity extends PhotoBaseActivity implements
        View.OnClickListener,
        PhotoListener.OnAImgSelectStateListener,
        PhotoListener.OnBtnCompleteListener,
        PhotoListener.OnPhotoItemClickListener {

    private final String TAG = "SelectImgActivity";
    //所有图片路径
    private static ArrayList<FileBean> mAllImagePathList;
    //暂存桶 存储当前选择相册图片
    private static ArrayList<FileBean> mImageBarrelList;
    //图片文件分类
    private static HashMap<String, ArrayList<FileBean>> mPhotoPathMapList;
    //存放图片选中状态
    protected static HashMap<File, Boolean> mImgSelStateSet = new HashMap<>();
    //设置最大选择数量 设目标值
    public static int SET_SELECT_MAX_NUM = 100;
    //是否可以选择图片 默认可以 超出选择数量 则等于false
    public static boolean IS_SELECT_IMG = true;

    //标题 图库名称
    private TextView mTvTitle;

    //显示图片选择列表
    private RecyclerView mRVAllImgView;
    private GridLayoutManager mSelectImgRvManager;
    private SelectImgAdapter mSelectImgAdapter;
    //相册，完成
    private Button mBtnPhoto, mBtnCompleteImgSel;
    //提示
    private ProgressBar mPbLoadHint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_image_view);
        findViews();
        initData();
        setListener();
    }

    private void initAdapter() {
        //初始化选中状态
        if (mImgSelStateSet == null) {
            mImgSelStateSet = new HashMap<>();
        }
        //不等才初始化 为全选 保留之前初始化状态 调用结束后 后需要调用 destroy() 方法 释放
        if (mAllImagePathList.size() != mImgSelStateSet.size()) {
            Set<String> set = mPhotoPathMapList.keySet();
            Iterator<String> iterator = set.iterator();

            while (iterator.hasNext()) {
                String photoName = iterator.next();
                ArrayList<FileBean> fileBeen = mPhotoPathMapList.get(photoName);

                //初始化全部未选中
                for (FileBean fb : fileBeen) {
                    mImgSelStateSet.put(fb.getImgFile(), false);
                }
            }
        } else {
            //初始化选中数量
            initSelectNum();
        }
        //图片列表 适配器
        mSelectImgAdapter = new SelectImgAdapter(this, mAllImagePathList, mImgSelStateSet, mPbLoadHint);
        mSelectImgRvManager = new GridLayoutManager(this, 3);
        mRVAllImgView.setAdapter(mSelectImgAdapter);
        mRVAllImgView.setLayoutManager(mSelectImgRvManager);

        //大图查看时 单张图片状态改变
        PhotoShowMaxImgActivity.setOnAImgSelectListener(this);
        //大图查看时 点击完成
        PhotoShowMaxImgActivity.setOnImgSelectOkListener(this);
        //列表单张图片 被选择时
        mSelectImgAdapter.setOnAImgSelectListener(this);
    }

    @Override
    public void initData() {
        mPbLoadHint.setVisibility(View.VISIBLE);
        new GetAllImagePath(this).getImageAllPath(new GetAllImagePath.OnSDImagePathLoadOkListener() {
            @Override
            public void onResponse(ArrayList<FileBean> allImagePathList) {
                if (allImagePathList == null) {
                    return;
                }
                mAllImagePathList = allImagePathList;
                mImageBarrelList = mAllImagePathList;
            }

            @Override
            public void onResponse(HashMap<String, ArrayList<FileBean>> photoPathMapSet) {
                if (photoPathMapSet == null) {
                    return;
                }
                mPhotoPathMapList = photoPathMapSet;
                initAdapter();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //打开小图缓存
        BitmapCache.mIsStopLoadMinImg = false;
        //大图查看切换回来时更新选中状态
        mPbLoadHint.setVisibility(View.VISIBLE);
        if (mSelectImgAdapter != null) {
            mSelectImgAdapter.addItemData(mImageBarrelList, mImgSelStateSet, mPbLoadHint);
        }
        if (mPhotoDialog != null) {
            mPhotoDialog.dismiss();
        }
    }

    @Override
    public void findViews() {
        mTvTitle = (TextView) findViewById(R.id.tv_title);

        mRVAllImgView = (RecyclerView) findViewById(R.id.rv_view_all_img);

        //相册
        mBtnPhoto = (Button) findViewById(R.id.btn_photo);
        //完成图片选择
        mBtnCompleteImgSel = (Button) findViewById(R.id.btn_complete_img_sel);
        mPbLoadHint = (ProgressBar) findViewById(R.id.pb_load_hint);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拿到已经选择好的图片
                getAlreadySelImg();
                finish();
            }
        });
    }

    @Override
    public void setListener() {
        //相册
        mBtnPhoto.setOnClickListener(this);
        //完成图片选择
        mBtnCompleteImgSel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_photo) {
            showPhotoDialog();
        }

        //完成 图片选择
        if (i == R.id.btn_complete_img_sel) {
            //拿到已经选择好的图片
            getAlreadySelImg();
        }
    }

    //相册 选择对话框
    private View mPhotoDialogLayout;
    private Dialog mPhotoDialog;

    private RecyclerView mRvPhotoItem;
    private LinearLayoutManager mPhotoManager;
    private PhotoListDialogAdapter mPhotoAdapter;

    private void showPhotoDialog() {
        //相册Item点击回调
        PhotoListDialogAdapter.setOnPhotoItemClickListener(this);

        if (mPhotoDialogLayout == null) {
            mPhotoDialogLayout = View.inflate(this, R.layout.photo_dialog_layout, null);
            mRvPhotoItem = (RecyclerView) mPhotoDialogLayout.findViewById(R.id.re_photo_popup_layout);
        }

        if (mPhotoDialog == null) {
            mPhotoDialog = new Dialog(this, R.style.dialog_theme);
        }

        mPhotoManager = new LinearLayoutManager(this);
        mRvPhotoItem.setLayoutManager(mPhotoManager);
        mPhotoAdapter = new PhotoListDialogAdapter(this, mPhotoPathMapList);
        mRvPhotoItem.setAdapter(mPhotoAdapter);

        mPhotoDialog.setContentView(mPhotoDialogLayout);
        Window dialogWindow = mPhotoDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        mPhotoDialog.show();
    }

    //相册被点击时回调
    @Override
    public void onPhotoItemClickListener(ArrayList<FileBean> photoImagePathList, String photoName) {
        mTvTitle.setText(photoName);
        mImageBarrelList = photoImagePathList;
        if (mSelectImgAdapter != null) {
            mSelectImgAdapter.addItemData(photoImagePathList, mImgSelStateSet, mPbLoadHint);
        }
        mPhotoDialog.dismiss();
    }

    //一张图片 状态选择改变 监听
    @Override
    public void onAImgSelectStateListener(File file, boolean imgIsSelect) {
        initImgSelectState(file, imgIsSelect);

    }

    //初始化选中状态
    private void initImgSelectState(File file, boolean imgIsSelect) {
        //更改状态
        mImgSelStateSet.put(file, imgIsSelect);
        initSelectNum();
    }

    //初始化选中数量
    private void initSelectNum() {
        int imgNum = 0;

        Set<File> set = mImgSelStateSet.keySet();
        Iterator<File> iterator = set.iterator();
        while (iterator.hasNext()) {
            if (mImgSelStateSet.get(iterator.next())) {
                imgNum++;
            }
        }

        if (imgNum == 0) {
            //设置选中数量
            mBtnCompleteImgSel.setText("完成");
        } else {
            //设置选中数量
            mBtnCompleteImgSel.setText("完成(" + imgNum + ")");
        }

        if (imgNum + 1 > SET_SELECT_MAX_NUM) {
            Utils.getUtils().toast(this, "最多可选择" + SET_SELECT_MAX_NUM + "张");
            IS_SELECT_IMG = false;
        } else {
            IS_SELECT_IMG = true;
        }
    }

    //查看大图 点击完成监听
    @Override
    public void onBtnCompleteListener() {
        getAlreadySelImg();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPhotoDialog != null) {
            mPhotoDialog.dismiss();
        }
    }


    //图片选择成功后回调
    private static PhotoListener.OnImgSelectOkListener mOnImgSelectOkListener;

    public static void setOnImgSelectOkListener(PhotoListener.OnImgSelectOkListener onImgSelectOkListener) {

        mOnImgSelectOkListener = onImgSelectOkListener;
    }


    //点击完成时回调
    public void getAlreadySelImg() {
        if (mImgSelStateSet == null || mAllImagePathList == null) {
            return;
        }

        ArrayList<File> finalFileList = new ArrayList<>();

        for (FileBean b : mAllImagePathList) {
            //如果该图片选中
            if (mImgSelStateSet.get(b.getImgFile())) {
                finalFileList.add(b.getImgFile());
            }
        }
        if (mOnImgSelectOkListener != null) {
            if (finalFileList.size() <= 0) {
                mOnImgSelectOkListener.onImgSelectOkListener(null);
            } else {
                mOnImgSelectOkListener.onImgSelectOkListener(finalFileList);
            }
        }
        finish();
    }

    //外部图片删除时 调用
    public static void setOnSelectImgDelete(File file) {
        //更改状态
        mImgSelStateSet.put(file, false);
    }

    //由外部 确定选择后 必须调用
    public static void destroy(boolean isClearBitmap) {
        if (isClearBitmap) {
            BitmapCache.destroy();
        }

        //所有图片集合
        if (mAllImagePathList != null) {
            mAllImagePathList.clear();
        }
        //相册分类集合
        if (mPhotoPathMapList != null) {
            mPhotoPathMapList.clear();
        }

        //图片选中状态集合
        if (mImgSelStateSet != null) {
            mImgSelStateSet.clear();
        }
        SET_SELECT_MAX_NUM = 100;
        IS_SELECT_IMG = true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            getAlreadySelImg();
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }


}