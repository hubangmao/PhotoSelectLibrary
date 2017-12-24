package com.hubangmao.photoselectlibrary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hubangmao.photoselectlibrary.R;
import com.hubangmao.photoselectlibrary.activity.listener.PhotoListener;
import com.hubangmao.photoselectlibrary.utils.BitmapCache;
import com.hubangmao.photoselectlibrary.utils.FileBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by 胡邦茂 on 2017/2/22.
 *
 * @ about 相册选择适配器
 */
//查看全部图片 适配器
public class PhotoListDialogAdapter extends RecyclerView.Adapter<PhotoListDialogHolder> {
    private final String TAG = "PhotoListDialogAdapter";
    private Context mContext;
    private HashMap<String, ArrayList<FileBean>> mPhotoPathMapList = new HashMap<>();

    private ArrayList<String> mMapKeyList = new ArrayList<>();
    private PhotoListener.OnPhotoItemClickListener mOnPhotoItemClickListener;

    public void setOnPhotoItemClickListener(PhotoListener.OnPhotoItemClickListener onPhotoItemClickListener) {
        mOnPhotoItemClickListener = onPhotoItemClickListener;
    }

    public PhotoListDialogAdapter(Context context, HashMap<String, ArrayList<FileBean>> photoPathMapList) {
        if (context == null || photoPathMapList == null) {
            return;
        }

        mContext = context;
        mPhotoPathMapList.putAll(photoPathMapList);
        Set<String> set = mPhotoPathMapList.keySet();
        mMapKeyList.addAll(set);

    }


    @Override
    public PhotoListDialogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotoListDialogHolder(LayoutInflater.from(mContext).inflate(R.layout.item_photo_select, parent, false));
    }

    public static String mSelPhotoName = "全部图片";
    private String mPhotoName;

    @Override
    public void onBindViewHolder(final PhotoListDialogHolder holder, final int position) {
        mPhotoName = mMapKeyList.get(position);
        ArrayList<FileBean> fileList = mPhotoPathMapList.get(mPhotoName);
        //第一张图片作为相册图片
        BitmapCache.getBitmapCache().setMinImgBitmap(fileList.get(0).getImgFile(), holder.mIvPhotoItem);
        mPhotoName = getSubPhotoName(mPhotoName);
        //等于选中的相册名称显示选中指示
        if (mSelPhotoName.equals(mPhotoName)) {
            holder.mIvSelectState.setVisibility(View.VISIBLE);
        } else {
            holder.mIvSelectState.setVisibility(View.GONE);
        }
        holder.mTvName.setText(mPhotoName + "(" + fileList.size() + ") 张");
        holder.mPhotoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelPhotoName = getSubPhotoName(mMapKeyList.get(position));
                mOnPhotoItemClickListener.onPhotoItemClickListener(mPhotoPathMapList.get(mMapKeyList.get(position)), mSelPhotoName);
            }
        });
    }

    private String getSubPhotoName(String defStr) {
        if (defStr.length() > 12) {
            defStr = defStr.substring(0, 12);
        }
        return defStr;
    }

    @Override
    public int getItemCount() {
        return mPhotoPathMapList == null ? 0 : mPhotoPathMapList.size();
    }

}

class PhotoListDialogHolder extends RecyclerView.ViewHolder {
    RelativeLayout mPhotoItem;
    ImageView mIvPhotoItem, mIvSelectState;
    TextView mTvName;


    public PhotoListDialogHolder(View itemView) {
        super(itemView);
        mPhotoItem = (RelativeLayout) itemView.findViewById(R.id.rl_photo_item);
        mIvPhotoItem = (ImageView) itemView.findViewById(R.id.iv_photo_icon);
        mIvSelectState = (ImageView) itemView.findViewById(R.id.iv_select_state);
        mTvName = (TextView) itemView.findViewById(R.id.tv_photo_name);
    }

}
