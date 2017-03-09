package com.hubangmao.photoselectlibrary.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.hubangmao.photoselectlibrary.R;
import com.hubangmao.photoselectlibrary.activity.listener.PhotoListener;
import com.hubangmao.photoselectlibrary.utils.BitmapCache;
import com.hubangmao.photoselectlibrary.utils.FileBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by 胡邦茂 on 2017/2/22.
 *
 * @ about 相册选择适配器
 */
//查看全部图片 适配器
public class PhotoListDialogAdapter extends RecyclerView.Adapter<PhotoListDialogHolder> {
    private  final String TAG ="PhotoListDialogAdapter";
    private Context mContext;
    private HashMap<String, ArrayList<FileBean>> mPhotoPathMapList = new HashMap<>();

    private ArrayList<String> mMapKeyList = new ArrayList<>();
    private static PhotoListener.OnPhotoItemClickListener mOnPhotoItemClickListener;

    public static void setOnPhotoItemClickListener(PhotoListener.OnPhotoItemClickListener mOnPhotoItemClickListener) {
        PhotoListDialogAdapter.mOnPhotoItemClickListener = mOnPhotoItemClickListener;
    }

    public PhotoListDialogAdapter(Context context, HashMap<String, ArrayList<FileBean>> photoPathMapList) {
        if (context == null || photoPathMapList == null) {
            return;
        }

        mContext = context;
        mPhotoPathMapList.putAll(photoPathMapList);


        Set<String> set = mPhotoPathMapList.keySet();
        Iterator<String> iterator = set.iterator();

        while (iterator.hasNext()) {
            mMapKeyList.add(iterator.next());
        }

    }


    @Override
    public PhotoListDialogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotoListDialogHolder(LayoutInflater.from(mContext).inflate(R.layout.item_photo_select, parent, false));
    }

    @Override
    public void onBindViewHolder(final PhotoListDialogHolder holder, final int position) {
        final String name = mMapKeyList.get(position);

        ArrayList<FileBean> fileList = mPhotoPathMapList.get(name);
        BitmapCache.getBitmapCache().setMinImgBitmap(fileList.get(0).getImgFile(), holder.mIvPhotoItem);
        holder.mTvName.setText(name + "(" + fileList.size() + ") 张");
        holder.mPhotoItem.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.photo_item_enter));

        holder.mPhotoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnPhotoItemClickListener.onPhotoItemClickListener(mPhotoPathMapList.get(mMapKeyList.get(position)), name);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mPhotoPathMapList == null ? 0 : mPhotoPathMapList.size();
    }

}

class PhotoListDialogHolder extends RecyclerView.ViewHolder {
    CardView mPhotoItem;
    ImageView mIvPhotoItem;
    TextView mTvName;


    public PhotoListDialogHolder(View itemView) {
        super(itemView);
        mPhotoItem = (CardView) itemView.findViewById(R.id.cv_photo_item);
        mIvPhotoItem = (ImageView) itemView.findViewById(R.id.iv_photo_icon);
        mTvName = (TextView) itemView.findViewById(R.id.tv_photo_name);
    }

}
