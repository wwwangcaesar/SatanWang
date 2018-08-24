package com.lovcreate.core.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 自定义ListView的工具类(用于ListView嵌套)
 * Created by Albert.Ma on 2017/5/10.
 */
public class ListViewUtil {
    /**
     * 在listview设定自定义adapter后计算每项adapter的高度,然后计算总的高度再设置
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
//            int desiredWidth= View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * listAdapter.getCount());
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 手动设置包含count个子项View的ListView的高度
     *
     * @param listView
     * @param count             子项View的个数
     * @param defaultItemHeight 一个子项View的高度
     */
    public static void setListViewHeightByChildrenCount(ListView listView, int count, int defaultItemHeight) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int itemHeight = defaultItemHeight;
        if (listAdapter.getCount() != 0) {
            View listItem = listAdapter.getView(0, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            itemHeight = listItem.getMeasuredHeight(); // 子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = itemHeight * count + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

}
