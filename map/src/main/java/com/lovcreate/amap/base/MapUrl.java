package com.lovcreate.amap.base;

import com.lovcreate.core.base.CoreUrl;

/**
 * 高德地图与后台的请求
 * Created by Albert.Ma on 2017/8/2 0002.
 */

public class MapUrl extends CoreUrl {
    // 发送坐标
    public static String sendLocation = baseUrl + "/coordinate/receive";
    // 登录
    public static String login = baseUrl + "/user/login";
    // 实时坐标查询
    public static String getTrueTime = baseUrl + "/coordinate/getTrueTime";
    // 查看轨迹
    public static String getTrack = baseUrl + "/coordinate/getTrack";
}
