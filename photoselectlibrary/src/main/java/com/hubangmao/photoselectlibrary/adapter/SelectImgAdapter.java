package com.hubangmao.photoselectlibrary.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.hubangmao.photoselectlibrary.R;
import com.hubangmao.photoselectlibrary.activity.PhotoShowMaxImgActivity;
import com.hubangmao.photoselectlibrary.activity.SelectImgActivity;
import com.hubangmao.photoselectlibrary.activity.listener.PhotoListener;
import com.hubangmao.photoselectlibrary.utils.BitmapCache;
import com.hubangmao.photoselectlibrary.bean.FileBean;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static com.hubangmao.photoselectlibrary.activity.PhotoShowMaxImgActivity.index;

/**
 * Created by 胡邦茂 on 2017/2/22.
 *
 * @ about 选择图片适配器
 */
//查看全部图片 适配器
public class SelectImgAdapter extends RecyclerView.Adapter<SelectImgHolder> {
    private final String TAG = "SelectImgAdapter";
    private Context mContext;
    private ArrayList<FileBean> mAllImagePathList = new ArrayList<>();
    private HashMap<File, Boolean> mImgSelStateSet = new HashMap<>();
    //一张图片 选择状态监听
    private PhotoListener.OnAImgSelectStateListener mOnImgSelectListener;

    public void setOnAImgSelectListener(PhotoListener.OnAImgSelectStateListener onImgSelectListener) {
        mOnImgSelectListener = onImgSelectListener;
    }

    public SelectImgAdapter(Context context, ArrayList<FileBean> allImagePathList, HashMap<File, Boolean> imgSelStateSet, ProgressBar pbLoadHint) {
        mContext = context;
        mAllImagePathList.addAll(allImagePathList);
        mImgSelStateSet.putAll(imgSelStateSet);
        initLoadImgThread(pbLoadHint);
    }

    public void addItemData(ArrayList<FileBean> allImagePathList, HashMap<File, Boolean> imgSelStateSet, ProgressBar pbLoadHint) {
        mAllImagePathList.clear();
        mAllImagePathList.addAll(allImagePathList);

        mImgSelStateSet.clear();
        mImgSelStateSet.putAll(imgSelStateSet);
        initLoadImgThread(pbLoadHint);
    }

    private void initLoadImgThread(final ProgressBar pbLoadHint) {
        notifyDataSetChanged();
        //打开小图缓存
        BitmapCache.mIsStopLoadMinImg = false;

        new Thread() {
            @Override
            public void run() {
                BitmapCache.getBitmapCache().initPhotoIconCache(mAllImagePathList, new BitmapCache.OnMinImgLoadListener() {
                    @Override
                    public void onMinImgLoadListener(final FileBean f, final int index) {
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (pbLoadHint.getVisibility() == View.VISIBLE) {
                                    pbLoadHint.setVisibility(View.GONE);
                                }
                                if (index >= mAllImagePathList.size()) {
                                    return;
                                }

                                mAllImagePathList.remove(index);
                                mAllImagePathList.add(index, f);
                                notifyItemChanged(index, f);
                            }
                        });
                    }
                });

            }
        }.start();


    }

    @Override
    public SelectImgHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectImgHolder(LayoutInflater.from(mContext).inflate(R.layout.item_image_select, parent, false));
    }

    @Override
    public void onBindViewHolder(final SelectImgHolder holder, final int position) {
        final FileBean fileBean = mAllImagePathList.get(position);
        Bitmap bitmap = fileBean.getBitmap();
        if (bitmap == null) {
            holder.mIvItem.setImageResource(R.mipmap.load_err_icon);
            holder.mCbSelImg.setChecked(false);
            return;
        }

        holder.mIvItem.setImageBitmap(bitmap);
        boolean cbSelState = mImgSelStateSet.get(fileBean.getImgFile());
        holder.mCbSelImg.setChecked(cbSelState);


        //选中显示灰色背景
        if (cbSelState) {
            holder.mKeepOutLayout.setVisibility(View.VISIBLE);
        } else {
            holder.mKeepOutLayout.setVisibility(View.GONE);
        }

        //图片点击查看大图
        holder.mIvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //停止小图缓存
                BitmapCache.mIsStopLoadMinImg = true;
                //查看File类型大图
                ArrayList<String> fileArrayList = new ArrayList<>();
                for (int i = 0; i < mAllImagePathList.size(); i++) {
                    fileArrayList.add(mAllImagePathList.get(i).getImgFile().getAbsolutePath());
                }

                Intent intent = new Intent(mContext, PhotoShowMaxImgActivity.class);
                intent.putStringArrayListExtra(PhotoShowMaxImgActivity.INTENT_URLS_ACTION, fileArrayList);

                index = position;
                mContext.startActivity(intent);
            }
        });

        //CheckBox 点击选择
        holder.mCbSelImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //超出选择数量 不可选择
                if (!SelectImgActivity.IS_SELECT_IMG) {
                    holder.mCbSelImg.setChecked(false);
                }

                //选中显示灰色背景
                if (holder.mCbSelImg.isChecked()) {
                    holder.mKeepOutLayout.setVisibility(View.VISIBLE);
                } else {
                    holder.mKeepOutLayout.setVisibility(View.GONE);
                }

                //保存选中状态
                mOnImgSelectListener.onAImgSelectStateListener(fileBean.getImgFile(), holder.mCbSelImg.isChecked());
            }
        });


    }


    @Override
    public int getItemCount() {
        return mAllImagePathList == null ? 0 : mAllImagePathList.size();
    }
}


class SelectImgHolder extends RecyclerView.ViewHolder {
    RelativeLayout mRlSelImgLayout;
    CheckBox mCbSelImg;
    ImageView mIvItem;
    //遮挡
    LinearLayout mKeepOutLayout;

    public SelectImgHolder(View itemView) {
        super(itemView);
        mRlSelImgLayout = (RelativeLayout) itemView.findViewById(R.id.rl_sel_img_layout);
        mKeepOutLayout = (LinearLayout) itemView.findViewById(R.id.keep_out_layout);
        mCbSelImg = (CheckBox) itemView.findViewById(R.id.cb_select_img);
        mIvItem = (ImageView) itemView.findViewById(R.id.iv_item);
    }

}
