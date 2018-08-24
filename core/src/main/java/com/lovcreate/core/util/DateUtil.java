package com.lovcreate.core.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bright on 2017/7/31 0031
 * 日期工具类
 */
public class DateUtil {

    public static Date parseString(String time, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Date formatDateByTime(Long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return parseString(sdf.format(time), format);
    }

    public static String formatString(String time, String format, String returnFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String s = "";
        try {
            s = new SimpleDateFormat(returnFormat).format(sdf.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static String formatTime(Long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(time);
    }

    public static Calendar getCalendarByString(String time, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parseString(time, format));
        return calendar;
    }

    public static long getLongTime(Date date, String format) {
        String time = formatDate(date, format);
        return parseString(time, format).getTime();
    }


    /**
     * @param year
     * @param month 从0开始，0代表一月
     * @param day
     * @return 返回的1代表周日，依次类推
     */
    public static int getWeekDay(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Log.d("DateView", "DateView:First:" + calendar.getFirstDayOfWeek());
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 通过年份和月份 得到当月的日子
     *
     * @param year
     * @param month
     * @return
     */
    public static int getMonthDays(int year, int month) {
        month++;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
                    return 29;
                } else {
                    return 28;
                }
            default:
                return -1;
        }
    }

    /**
     * 返回当前月份1号位于周几
     *
     * @param year  年份
     * @param month 月份，传入系统获取的，不需要正常的
     * @return 日：1 一：2 二：3 三：4 四：5 五：6 六：7
     */
    public static int getFirstDayWeek(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        Log.d("DateView", "DateView:First:" + calendar.getFirstDayOfWeek());
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取忽略24小时为天的年月日格式字符串
     *
     * @param time 毫秒
     * @return 34:50:50
     */
    public static String getStringTimeIgnoreDay(long time) {
        time = time / 1000;
        long h = time / 60 / 60;
        long m = (time / 60) % 60;
        long s = time % 60;
        String hour = h > 9 ? String.valueOf(h) : "0" + h;
        String minute = m > 9 ? String.valueOf(m) : "0" + m;
        String second = s > 9 ? String.valueOf(s) : "0" + s;
        if (Double.valueOf(hour) == 0) {
            return minute + ":" + second;
        }
        return hour + ":" + minute + ":" + second;
    }

    /**
     * 获取忽略24小时为天的年月日格式字符串
     *
     * @param time 毫秒
     * @return 34:50:50
     */
    public static Map<String, String> getMapTimeIgnoreDay(long time) {
        Map<String, String> map = new HashMap<>();
        time = time / 1000;
        long h = time / 60 / 60;
        long m = (time / 60) % 60;
        long s = time % 60;
        String hour = h > 9 ? String.valueOf(h) : "0" + h;
        String minute = m > 9 ? String.valueOf(m) : "0" + m;
        String second = s > 9 ? String.valueOf(s) : "0" + s;
        map.put("hour", hour);
        map.put("minute", minute);
        map.put("second", second);
        return map;
    }

    /**
     * 获取忽略24小时为天的毫秒数
     *
     * @param time 时分秒字符串 HH:mm:ss
     * @return 毫秒
     */
    public static long getLongTimeIgnoreDay(String time) {
        String[] arr = time.split(":");
        if (arr.length == 3) {
            long h = Long.valueOf(arr[0]);
            long m = Long.valueOf(arr[1]);
            long s = Long.valueOf(arr[2]);
            return (((h * 60) + m) * 60 + s) * 1000;
        } else if (arr.length == 2) {
            long m = Long.valueOf(arr[0]);
            long s = Long.valueOf(arr[1]);
            return ((m * 60) + s) * 1000;
        } else {
            return Long.valueOf(arr[0]) * 1000;
        }
    }

    public static String getNowDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }
}
