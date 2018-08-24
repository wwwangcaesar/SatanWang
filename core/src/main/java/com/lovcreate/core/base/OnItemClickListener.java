package com.lovcreate.core.base;

import android.view.View;
import android.widget.AdapterView;

import java.util.Calendar;

/**
 * 作者：yuanYe创建于2016/12/20
 * QQ：962851730
 *
 * 实现OnItemClickListener, 重写onItemClick()方法防止出现连点情况
 * 建议通过内部类方式实现
 */
public abstract class OnItemClickListener implements AdapterView.OnItemClickListener {

    // 两次点击事件的时间 间隔
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    // 最后一次点击事件
    private long lastClickTime = 0;

    /**
     * 防止出现多次点击
     */
    protected abstract void onItemNoDoubleClick(AdapterView<?> parent, View view, int position, long id);

    /**
     * 重写onItemClick方法, 1 秒内点击只生效一次
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onItemNoDoubleClick(parent, view, position, id);
        }
    }

}
