package com.lovcreate.core.constant;


/**
 * Core静态变量
 * Created by Albert.Ma on 2017/8/3 0003.
 */
public class CoreConstant {

    public static final String TOKEN = "100";
    public static final String LOGIN_OTHERS = "-1"; // 未找到该用户或您的账号在其他设备登录
    public static final String USER_LOCK_ERROR = "102"; // 您的账号被冻结
    public static final String VERIFY_CODE_FAIL = "29";

    public static final String token = "token";//token

    public static final String DEVICE_TYPE = "1";// 设备标识 1:android 2:ios

    /**
     * Content-Type
     */
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLYCATION = "application";
    public static final String JSON = "json";

    /**
     * OkHttp
     */
    public static final int OKHTTP_TIMEOUT = 5 * 60;//5分钟

    public static final String STUDENT_FORGET_PASSWORD = "STUDENT_FORGET_PWD";
    public static final String WARNING = "WARNING";
    /**
     * 网络请求返回状态
     */
    public static final String OK = "0";
    public static final String ERROR = "-1";

    /**
     * 分页数:每页条目数
     */
    public static final String PAGE_SIZE = "10";

    /**
     * Glide图片:图片缓存最大容量
     */
    public static final int GLIDE_CACHE_SIZE = 150 * 1000 * 1000;//150M

    /**
     * Glide图片:图片缓存子目录
     */
    public static final String GLIDE_CACHE_DIR = "glide_cache";

    /**
     * 腾讯Bugly App ID
     */
    public static final String BUGLY_APP_ID = "f885720b8d";

}
