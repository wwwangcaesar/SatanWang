package com.lovcreate.core.util;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.lovcreate.core.R;
import com.lovcreate.core.base.BaseActivity;

import java.util.Calendar;

/**
 * 统一 防IOS选择器:包括,日期,时间,条件,日期范围
 * Created by Albert.Ma on 2017/7/25 0025.
 */

public class IOSPickerUtil {

    /**
     * 收起键盘
     *
     * @param context
     */
    private static void hideSoftInput(Context context) {
        ((BaseActivity) context).hideSoftInput();
    }

    /**
     * 获取日期选择器Builder
     *
     * @param context              上下文
     * @param onTimeSelectListener 点击时间监听器
     * @param title                标题
     * @return
     */
    public static TimePickerView.Builder getDatePickerBuilder(Context context, TimePickerView.OnTimeSelectListener onTimeSelectListener, String title) {
        hideSoftInput(context);
        TimePickerView.Builder builder = new TimePickerView.Builder(context, onTimeSelectListener);
        builder.setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("完成")//确认按钮文字
                .setContentSize(18)//滚轮文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleText(title)//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
//                .setSubmitColor(ContextCompat.getColor(context, R.color.appBule))//确定按钮文字颜色
//                .setDividerColor(ContextCompat.getColor(context, R.color.appPalePurple))
                .setCancelColor(Color.GRAY)//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
//                .setBgColor(0xFF333333)//滚轮背景颜色 Night mode
//                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
//                .setRangDate(startDate,endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "", "", "")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false);//是否显示为对话框样式
        return builder;
    }

    /**
     * 获取时间选择器Builder
     *
     * @param context              上下文
     * @param onTimeSelectListener 点击时间监听器
     * @param title                标题
     * @return
     */
    public static TimePickerView.Builder getTimePickerBuilder(Context context, TimePickerView.OnTimeSelectListener onTimeSelectListener, String title) {
        hideSoftInput(context);
        TimePickerView.Builder builder = new TimePickerView.Builder(context, onTimeSelectListener);
        builder.setType(new boolean[]{false, false, false, true, true, false})// 默认全部显示
//                .setCancelText("取消")//取消按钮文字
                .setSubmitText("完成")//确认按钮文字
                .setContentSize(18)//滚轮文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleText(title)//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
//                .setSubmitColor(R.color.appPurple)//确定按钮文字颜色
//                .setCancelColor(Color.GRAY)//取消按钮文字颜色
//                .setTitleBgColor(R.color.appPalePurple)//标题背景颜色 Night mode
//                .setBgColor(0xFF333333)//滚轮背景颜色 Night mode
//                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
//                .setRangDate(startDate,endDate)//起始终止年月日设定
                .setLabel("", "", "", "", "", "")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false);//是否显示为对话框样式
        return builder;
    }

    /**
     * 单项条件选择器
     *
     * @param context                 上下文
     * @param onOptionsSelectListener 点击提交监听事件
     * @param title                   标题
     */
    public static OptionsPickerView.Builder getOptionsPickerBuilder(Context context, OptionsPickerView.OnOptionsSelectListener onOptionsSelectListener, String title) {
        hideSoftInput(context);
        OptionsPickerView.Builder builder = new OptionsPickerView.Builder(context, onOptionsSelectListener);
        builder.setTitleText(title)//设置标题内容
                .setTitleColor(Color.parseColor("#393845"))// 设置标题文字颜色
                .setContentTextSize(24)// 设置滚轮文字大小
                .setTextColorCenter(Color.parseColor("#393845"))//设置滚轮文字颜色
                .setDividerColor(Color.parseColor("#E6E6E6"))// 设置分割线的颜色
                .setBgColor(Color.WHITE)// 设置背景颜色
                .setTitleBgColor(Color.WHITE)// 设置标题背景颜色
                .setCancelColor(Color.parseColor("#9B96A3"))// 设置左侧取消文字颜色
                .setSubmitText("完成")//设置确定的文字内容
                .setSubmitColor(Color.parseColor("#E60012"))//设置确定的文字颜色
                .setBackgroundId(0x66000000) // 设置外部遮罩颜色
                .setLineSpacingMultiplier(0.5f)//滚轮间距设置（1.2-2.0倍，此为文字高度的间距倍数）
                .isCenterLabel(false);// 是否只显示中间选中项的label文字，false则每项item全部都带有label。
        return builder;
    }

}
