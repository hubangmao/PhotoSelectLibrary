package com.hubangmao.photoselectlibrary.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.hubangmao.photoselectlibrary.R;


/**
 * 介绍:提示对话框
 * author:胡邦茂
 * CreateDate:2017年09月08日 上午 9:49
 */

public class HintDialogUtil {
    //对话框消息
    private String mMsgStr;
    private String mPerformBtnStr = "确定";
    private String mCloseBtnStr = "取消";

    private DialogClickListener mListener;

    //是否可关闭 默认可关闭
    private boolean mIsClose = true;


    /**
     * @param msg 消息
     */
    public HintDialogUtil(String msg) {
        mMsgStr = msg;
    }

    /**
     * @param msgStr        消息
     * @param performBtnStr 执行按钮Text
     * @param closeBtnStr   取消按钮Text
     */
    public HintDialogUtil(String msgStr, String performBtnStr, String closeBtnStr) {
        mMsgStr = msgStr;
        mPerformBtnStr = performBtnStr;
        mCloseBtnStr = closeBtnStr;
    }

    //对话框执行按钮点击回调
    public interface DialogClickListener {
        void dialogPerformBtnClickListener(Dialog dialog);

        void dialogCloseBtnClickListener();
    }

    //对话框消息
    public HintDialogUtil setDialogMsg(String msg) {
        mMsgStr = msg;
        return this;
    }

    //执行按钮
    public HintDialogUtil setPerformBtnStr(String performBtnStr) {
        mPerformBtnStr = performBtnStr;
        return this;
    }

    //关闭按钮
    public HintDialogUtil setCloseBtnStr(String closeBtnStr) {
        mCloseBtnStr = closeBtnStr;
        return this;
    }

    public HintDialogUtil setIsClose(boolean close) {
        mIsClose = close;
        return this;
    }

    private Dialog mHintDialog;

    public void show(Activity activity) {
        show(activity, null);
    }

    public void show(Activity activity, DialogClickListener listener) {
        if (activity == null) {
            return;
        }

        mListener = listener;
        View hintDialogView = View.inflate(activity, R.layout.hint_dialog_layout, null);
        TextView tvMsg = (TextView) hintDialogView.findViewById(R.id.tv_msg);
        TextView tvDialogPerform = (TextView) hintDialogView.findViewById(R.id.tv_perform);
        TextView tvDialogClose = (TextView) hintDialogView.findViewById(R.id.tv_close_dialog);

        tvMsg.setText(mMsgStr);
        //执行按钮
        tvDialogPerform.setText(mPerformBtnStr);
        //关闭对话框 按钮
        tvDialogClose.setText(mCloseBtnStr);

        mHintDialog = new Dialog(activity, R.style.dialog_theme);
        mHintDialog.setContentView(hintDialogView);
        if (!mIsClose) {
            mHintDialog.setCancelable(false);
        }

        Window dialogWindow = mHintDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setWindowAnimations(R.style.hint_dialog_anim); // 添加动画
        mHintDialog.show();

        //执行按钮被点击
        tvDialogPerform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener == null) {
                    mHintDialog.cancel();
                } else {
                    mListener.dialogPerformBtnClickListener(mHintDialog);
                }

            }
        });

        tvDialogClose.findViewById(R.id.tv_close_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHintDialog.cancel();
                if (mListener != null)
                    mListener.dialogCloseBtnClickListener();
            }
        });

    }

    public void dismiss() {
        if (mHintDialog == null) {
            return;
        }
        mHintDialog.dismiss();
    }

}
