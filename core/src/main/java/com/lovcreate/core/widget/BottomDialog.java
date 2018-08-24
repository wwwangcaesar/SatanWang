package com.lovcreate.core.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.lovcreate.core.R;

/**
 * Created by Bright Lee on 2017/9/7
 * 自定义Dialog 底部弹出
 * 需要有自定义的style:
 * <style name="MyDialog">
 * --无标题--
 * <item name="android:windowNoTitle">true</item>
 * --无边框--
 * <item name="android:windowFrame">@null</item>
 * --悬浮在Activity上--
 * <item name="android:windowIsFloating">true</item>
 * --阴影半透明--
 * <item name="android:windowIsTranslucent">true</item>
 * </style>
 */
public class BottomDialog extends AlertDialog {
    Context mContext;

    public BottomDialog(Context context) {
        super(context); // 自定义全屏style
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void show() {
        super.show();
        //设置弹窗在屏幕底部
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        //设置弹窗宽度
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = window.getWindowManager().getDefaultDisplay().getWidth();
        window.setAttributes(lp);//设置宽度
    }
}