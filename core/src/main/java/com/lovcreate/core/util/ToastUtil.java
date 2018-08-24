package com.lovcreate.core.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.lovcreate.core.R;

/**
 * Toast工具类
 * <p>
 * 根据
 * https://github.com/jjdxmashl/jjdxm_dialogui
 * 修改完善
 * <p>
 * Created by Bright on 2018/2/11.
 */
public class ToastUtil {

    /**
     * 全局上下文
     */
    private static Context appContext;

    /**
     * 初始化
     */
    public static void init(Context appContext) {
        ToastUtil.appContext = appContext;
    }

    /**
     * 系统默认 - 长时间显示
     */
    public static void showToastDefaultLong(String text) {
        Toast.makeText(appContext, text, Toast.LENGTH_LONG).show();
    }

    /**
     * 系统默认 - 短时间显示
     */
    public static void showToastDefaultShort(String text) {
        Toast.makeText(appContext, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间居中显示
     */
    public static void showToastCenterLong(String text) {
        Toast toast = Toast.makeText(appContext, text, Toast.LENGTH_LONG);
        LayoutInflater inflate = (LayoutInflater)
                appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout.dialogui_toast, null);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setText(text);
        toast.show();
    }

    /**
     * 短时间居中显示
     */
    public static void showToastCenterShort(String text) {
        Toast toast = Toast.makeText(appContext, text, Toast.LENGTH_SHORT);
        LayoutInflater inflate = (LayoutInflater)
                appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout.dialogui_toast, null);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setText(text);
        toast.show();
    }

    /**
     * 长时间底部显示
     */
    public static void showToastBottomLong(String text) {
        Toast toast = Toast.makeText(appContext, text, Toast.LENGTH_LONG);
        LayoutInflater inflate = (LayoutInflater)
                appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout.dialogui_toast, null);
        toast.setView(view);
        toast.setGravity(Gravity.BOTTOM, 0, 230);
        toast.setText(text);
        toast.show();
    }

    /**
     * 短时间底部显示
     */
    public static void showToastBottomShort(String text) {
        Toast toast = Toast.makeText(appContext, text, Toast.LENGTH_SHORT);
        LayoutInflater inflate = (LayoutInflater)
                appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout.dialogui_toast, null);
        toast.setView(view);
        toast.setGravity(Gravity.BOTTOM, 0, 230);
        toast.setText(text);
        toast.show();
    }

    /**
     * 长时间顶部显示
     */
    public static void showToastTopLong(String text) {
        Toast toast = Toast.makeText(appContext, text, Toast.LENGTH_LONG);
        LayoutInflater inflate = (LayoutInflater)
                appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout.dialogui_toast, null);
        toast.setView(view);
        toast.setGravity(Gravity.TOP, 0, 64);
        toast.setText(text);
        toast.show();
    }

    /**
     * 短时间顶部显示
     */
    public static void showToastTopShort(String text) {
        Toast toast = Toast.makeText(appContext, text, Toast.LENGTH_SHORT);
        LayoutInflater inflate = (LayoutInflater)
                appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout.dialogui_toast, null);
        toast.setView(view);
        toast.setGravity(Gravity.TOP, 0, 64);
        toast.setText(text);
        toast.show();
    }

    /**
     * 系统默认 - 长时间显示
     */
    public static void showToastDefaultLong(final int resId) {
        Toast.makeText(appContext, resId, Toast.LENGTH_LONG).show();
    }

    /**
     * 系统默认 - 短时间显示
     */
    public static void showToastDefaultShort(final int resId) {
        Toast.makeText(appContext, resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间居中显示
     */
    public static void showToastCenterLong(final int resId) {
        Toast toast = Toast.makeText(appContext, resId, Toast.LENGTH_LONG);
        LayoutInflater inflate = (LayoutInflater)
                appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout.dialogui_toast, null);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setText(resId);
        toast.show();
    }

    /**
     * 短时间居中显示
     */
    public static void showToastCenterShort(final int resId) {
        Toast toast = Toast.makeText(appContext, resId, Toast.LENGTH_SHORT);
        LayoutInflater inflate = (LayoutInflater)
                appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout.dialogui_toast, null);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setText(resId);
        toast.show();
    }

    /**
     * 长时间底部显示
     */
    public static void showToastBottomLong(final int resId) {
        Toast toast = Toast.makeText(appContext, resId, Toast.LENGTH_LONG);
        LayoutInflater inflate = (LayoutInflater)
                appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout.dialogui_toast, null);
        toast.setView(view);
        toast.setGravity(Gravity.BOTTOM, 0, 230);
        toast.setText(resId);
        toast.show();
    }

    /**
     * 短时间底部显示
     */
    public static void showToastBottomShort(final int resId) {
        Toast toast = Toast.makeText(appContext, resId, Toast.LENGTH_SHORT);
        LayoutInflater inflate = (LayoutInflater)
                appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout.dialogui_toast, null);
        toast.setView(view);
        toast.setGravity(Gravity.BOTTOM, 0, 230);
        toast.setText(resId);
        toast.show();
    }

    /**
     * 长时间顶部显示
     */
    public static void showToastTopLong(final int resId) {
        Toast toast = Toast.makeText(appContext, resId, Toast.LENGTH_LONG);
        LayoutInflater inflate = (LayoutInflater)
                appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout.dialogui_toast, null);
        toast.setView(view);
        toast.setGravity(Gravity.TOP, 0, 64);
        toast.setText(resId);
        toast.show();
    }

    /**
     * 短时间顶部显示
     */
    public static void showToastTopShort(final int resId) {
        Toast toast = Toast.makeText(appContext, resId, Toast.LENGTH_SHORT);
        LayoutInflater inflate = (LayoutInflater)
                appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout.dialogui_toast, null);
        toast.setView(view);
        toast.setGravity(Gravity.TOP, 0, 64);
        toast.setText(resId);
        toast.show();
    }

    /**
     * 短时间中间显示成功Toast
     */
    public static void showSuccessToastCenterShort(String text) {
        Toast toast = Toast.makeText(appContext, text, Toast.LENGTH_SHORT);
        LayoutInflater inflate = (LayoutInflater)
                appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout.dialogui_toast_success, null);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setText(text);
        toast.show();
    }

}
