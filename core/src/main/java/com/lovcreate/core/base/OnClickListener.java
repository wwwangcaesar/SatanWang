package com.lovcreate.core.base;

import android.view.View;

import java.util.Calendar;

/**
 * 作者：yuanYe创建于2016/12/17
 * QQ：962851730
 *
 * 实现OnClickListener, 重写onClick()方法防止出现连点情况
 * 建议通过内部类方式实现
 */
public abstract class OnClickListener implements View.OnClickListener {

    // 两次点击事件的时间 间隔
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    // 最后一次点击时间
    private long lastClickTime = 0;

    /**
     * 防止出现多次点击
     */
    public abstract void onNoDoubleClick(View v);

    /**
     * 重写onClick()方法, 限制1 秒内只能点击一次
     */
    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }
}
