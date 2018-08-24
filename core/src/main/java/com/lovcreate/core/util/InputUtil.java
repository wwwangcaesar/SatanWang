package com.lovcreate.core.util;

import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * 输入框工具类
 * <p>
 * Created by Bright on 2018/1/10 0010.
 */
public class InputUtil {

    /**
     * 判定是否需要隐藏软键盘
     */
    public static boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

}
