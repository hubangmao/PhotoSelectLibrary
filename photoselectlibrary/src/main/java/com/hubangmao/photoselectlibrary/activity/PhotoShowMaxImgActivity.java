package com.hubangmao.photoselectlibrary.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hubangmao.photoselectlibrary.R;
import com.hubangmao.photoselectlibrary.activity.listener.PhotoListener;
import com.hubangmao.photoselectlibrary.utils.BitmapCache;
import com.hubangmao.photoselectlibrary.utils.FileBean;
import com.hubangmao.photoselectlibrary.zoom.FlexibleImageView;
import com.hubangmao.photoselectlibrary.zoom.PhotoViewAttacher;
import com.hubangmao.photoselectlibrary.zoom.ViewPagerFixed;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


/**
 * Created by 胡邦茂 on 2017/2/22.
 * 功能说明:查看大图,传入string类型的ArrayList,文件路径
 */
public class PhotoShowMaxImgActivity extends PhotoBaseActivity {
    private final String TAG = "PhotoShowMaxImgActivity";
    //intent key
    public static final String INTENT_URLS_ACTION = "uris";
    //ViewPage指针
    public static int index;

    //图片url
    private ArrayList<String> mFileBeanList = new ArrayList<>();
    private ViewPagerFixed mViewPager;
    private ImagePagerAdapter mImagePagerAdapter;
    private RelativeLayout mActionBarLayout, mBottomToolLayout;
    private TextView mTvName, mTvHint;
    //完成
    private Button mBtnComplete;
    private CheckBox mCbSelImg;
    private ProgressBar mPbHint;
    //一张图片 选择状态监听
    private static PhotoListener.OnAImgSelectStateListener mOnAImgSelectListener;

    public static void setOnAImgSelectListener(PhotoListener.OnAImgSelectStateListener onImgSelectListener) {
        mOnAImgSelectListener = onImgSelectListener;
    }

    //点击完成监听
    private static PhotoListener.OnBtnCompleteListener mOnImgSelectOkListener;

    public static void setOnImgSelectOkListener(PhotoListener.OnBtnCompleteListener onImgSelectOkListener) {
        mOnImgSelectOkListener = onImgSelectOkListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_look_max_images);
        findViews();
        initAdapter();
        setListener();

    }

    private void initAdapter() {
        mFileBeanList = (ArrayList<String>) getIntent().getSerializableExtra(INTENT_URLS_ACTION);

        if (mFileBeanList == null) {
            return;
        }
        mImagePagerAdapter = new ImagePagerAdapter(mFileBeanList);
        mViewPager.setAdapter(mImagePagerAdapter);
        //图片名称显示
        if (mFileBeanList.size() > 0) {
            String s = mFileBeanList.get(0);
            mTvName.setText(s.substring(s.lastIndexOf("/") + 1, s.length()));
        }

        //指针小于等于集合
        if (index <= mFileBeanList.size()) {
            mViewPager.setCurrentItem(index);
        }
        //初始化当前位置
        mTvHint.setText(index + 1 + "/" + mFileBeanList.size());

        //初始化当前选中
        HashMap<File, Boolean> imgSelStateSet = SelectImgActivity.mImgSelStateSet;
        mCbSelImg.setChecked(imgSelStateSet.get(new File(mFileBeanList.get(mViewPager.getCurrentItem()))));

        //初始化选中数量
        initSelectNum();

    }

    @Override
    public void findViews() {
        mViewPager = (ViewPagerFixed) findViewById(R.id.view_page);
        mActionBarLayout = (RelativeLayout) findViewById(R.id.action_bar_layout);
        mBottomToolLayout = (RelativeLayout) findViewById(R.id.bottom_tool_layout);
        mTvHint = (TextView) findViewById(R.id.tv_hint);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mPbHint = (ProgressBar) findViewById(R.id.pb_hint);
        mBtnComplete = (Button) findViewById(R.id.btn_complete_img_sel);
        mCbSelImg = (CheckBox) findViewById(R.id.cb_select_img);
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void setListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mFileBeanList.size() > 0) {
                    mTvHint.setText(position + 1 + "/" + mFileBeanList.size());
                }
                if (mFileBeanList.size() > 0) {
                    String s = mFileBeanList.get(position);
                    mTvName.setText(s.substring(s.lastIndexOf("/") + 1, s.length()));
                }

                //CheckBox 是否选中
                HashMap<File, Boolean> imgSelStateSet = SelectImgActivity.mImgSelStateSet;
                mCbSelImg.setChecked(imgSelStateSet.get(new File(mFileBeanList.get(position))));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mCbSelImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //超出选择数量 不可选择
                if (!SelectImgActivity.IS_SELECT_IMG) {
                    mCbSelImg.setChecked(false);
                }

                //拿到当前图片File
                File imgPath = new File(mFileBeanList.get(mViewPager.getCurrentItem()));
                //保存选中状态
                mOnAImgSelectListener.onAImgSelectStateListener(imgPath, mCbSelImg.isChecked());
                //初始化选中数量
                initSelectNum();
            }
        });
        //完成
        mBtnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                mOnImgSelectOkListener.onBtnCompleteListener();
            }
        });
    }

    private void initSelectNum() {
        int imgNum = 0;

        Set<File> set = SelectImgActivity.mImgSelStateSet.keySet();
        Iterator<File> iterator = set.iterator();
        while (iterator.hasNext()) {
            if (SelectImgActivity.mImgSelStateSet.get(iterator.next())) {
                imgNum++;
            }
        }

        //设置选中数量
        mBtnComplete.setText("完成(" + imgNum + ")");
    }

    private boolean mIsShowToolBar;

    private class ImagePagerAdapter extends PagerAdapter {
        private ArrayList<String> mAllImgList;
        private ArrayList<FlexibleImageView> mFILists;
        private Activity mActivity = PhotoShowMaxImgActivity.this;

        private ImagePagerAdapter(ArrayList<String> imgAllList) {
            mPbHint.setVisibility(View.VISIBLE);
            mAllImgList = imgAllList;
            mFILists = new ArrayList<>();
            for (int i = 0; i < mAllImgList.size(); i++) {
                mFILists.add(new FlexibleImageView(mActivity));
            }
        }


        @Override
        public int getCount() {
            return mAllImgList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mFILists.get(position));
        }

        @Override
        public FlexibleImageView instantiateItem(final ViewGroup container, final int position) {
            mPbHint.setVisibility(View.VISIBLE);
            final FlexibleImageView flexibleImageView = mFILists.get(position);

            BitmapCache.getBitmapCache().asyReduceMaxImageSize(new File(mAllImgList.get(position)), new BitmapCache.OnMaxImgLoadListener() {
                @Override
                public void onMaxImgLoadListener(final FileBean b) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPbHint.setVisibility(View.GONE);
                            flexibleImageView.setImageBitmap(b.getBitmap());

                            if (flexibleImageView.getParent() == container) {
                                container.removeView(flexibleImageView);
                            }
                            container.addView(flexibleImageView);

                        }
                    });
                }
            });
            flexibleImageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    if (mIsShowToolBar) {
                        mActionBarLayout.setVisibility(View.VISIBLE);
                        mBottomToolLayout.setVisibility(View.VISIBLE);
                        AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
                        animation.setDuration(400);
                        mActionBarLayout.setAnimation(animation);
                        mBottomToolLayout.setAnimation(animation);
                        mIsShowToolBar = !mIsShowToolBar;
                    } else {
                        mActionBarLayout.setVisibility(View.GONE);
                        mBottomToolLayout.setVisibility(View.GONE);
                        AlphaAnimation animation = new AlphaAnimation(1.0f, 0.1f);
                        animation.setDuration(300);
                        mActionBarLayout.setAnimation(animation);
                        mBottomToolLayout.setAnimation(animation);
                        mIsShowToolBar = !mIsShowToolBar;
                    }
                }
            });
            return flexibleImageView;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        index = 0;
    }
}
