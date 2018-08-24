package com.lovcreate.core.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Created by Bright on 2017/7/31 0031
 * 自定义GridView，解决在ScrollView中GridView只显示一行的问题
 * <p>
 * 在这个重写改造后的GridView里面增加了判断是否完全伸展开的状态值，可以set是否完全伸展。
 * 如果expanded为true，那么初始化加载后就完全展开这个GridView的所有子item；
 * 如果expanded为false，那么就退化成Android原生的GridView只显示一行。
 * expanded默认为true
 */
public class MyGridView extends GridView {

    boolean expanded = true;

    public boolean isExpanded() {
        return expanded;
    }

    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isExpanded()) {
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);
            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}