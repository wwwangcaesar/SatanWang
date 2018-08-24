package com.lovcreate.core.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.lovcreate.core.R;

/**
 * Created by Li on 2017/2/7.
 */

public class DialogUtil {
    private static AlertDialog.Builder mDialogBuilder;
    private static AlertDialog alert;

    /**
     * 加载中弹框
     *
     * @param context
     * @return
     */
    public static Dialog buildLoading(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View root = View.inflate(context, R.layout.dialogui_loading_horizontal, null);
        View llBg = (View) root.findViewById(R.id.dialogui_ll_bg);
        ProgressBar pbBg = (ProgressBar) root.findViewById(R.id.pb_bg);
//        TextView tvMsg = (TextView) root.findViewById(R.id.dialogui_tv_msg);
//        tvMsg.setText(bean.msg);
        llBg.setBackgroundResource(R.drawable.dialogui_shape_wihte_round_corner);
        pbBg.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.dialogui_rotate_mum));
//            tvMsg.setTextColor(bean.mContext.getResources().getColor(R.color.text_black));
        // 禁用外部消失
        dialog.setCanceledOnTouchOutside(false);
        // 禁用返回键
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;//不执行父类点击事件
                }
                return false;
            }
        });
        dialog.setContentView(root);
        return dialog;
    }

    public static Dialog showCustomDialog(final Context context, final View view) {
        /* 加载dialog布局 */
        mDialogBuilder = new AlertDialog.Builder(context, R.style.dialog);
        mDialogBuilder.setView(view);
        mDialogBuilder.setCancelable(true);
        alert = mDialogBuilder.create();
        Window dialogWindow = alert.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
        // WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        // lp.y = 20;
        // dialogWindow.setAttributes(lp);
        return alert;
    }

    public static Dialog showBottomDialog(final Context context, final View view) {
        /* 加载dialog布局 */
        mDialogBuilder = new AlertDialog.Builder(context, R.style.dialog);
        mDialogBuilder.setView(view);
        mDialogBuilder.setCancelable(true);
        alert = mDialogBuilder.create();
        Window dialogWindow = alert.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
        alert.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;
        dialogWindow.setAttributes(lp);
        return alert;
    }

    public static Dialog number(final Context context, final View view) {
        /* 加载dialog布局 */
        mDialogBuilder = new AlertDialog.Builder(context, R.style.DialogTheme);
        mDialogBuilder.setView(view);
        mDialogBuilder.setCancelable(true);
        alert = mDialogBuilder.create();
        Window dialogWindow = alert.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
        alert.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;
        dialogWindow.setAttributes(lp);
        return alert;
    }

    public static Dialog showCustomAddDialog(final Context context, final View view) {
        /* 加载dialog布局 */
        mDialogBuilder = new AlertDialog.Builder(context, R.style.dialog);
        mDialogBuilder.setView(view);
        mDialogBuilder.setCancelable(true);
        alert = mDialogBuilder.create();
        Window dialogWindow = alert.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        alert.setCanceledOnTouchOutside(true);
        alert.setCancelable(true);
        // WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        // lp.y = 20;
        // dialogWindow.setAttributes(lp);
        return alert;
    }

    public static Dialog showCustomAddDialog_01(final Context context, final View view) {
        /* 加载dialog布局 */
        mDialogBuilder = new AlertDialog.Builder(context, R.style.dialog);
        mDialogBuilder.setView(view);
        mDialogBuilder.setCancelable(true);
        alert = mDialogBuilder.create();
        Window dialogWindow = alert.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        alert.setCanceledOnTouchOutside(true);
        alert.setCancelable(true);
        // WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        // lp.y = 20;
        // dialogWindow.setAttributes(lp);
        return alert;
    }
}
