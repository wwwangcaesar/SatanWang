package com.lovcreate.core.util;


import com.lovcreate.core.app.CoreApplication;

/**
 * App相关的内容,例如登录人信息,主要是存放在SharedPreferences中,这里对相关信息的添加获取进行封装
 * <p>
 * Created by Albert.Ma on 2017/10/27
 */
public class AppSession {

    /**
     * 设置是否接受推送消息
     */
    public static void setMessageReceive(boolean messageReceive) {
        SharedPreferencesUtil.saveData(CoreApplication.getInstance().getApplicationContext(), "isMessageReceive", messageReceive);
    }

    /**
     * 设置阿里云推送设备号 deviceId
     */
    public static void setDeviceId(String deviceId) {
        SharedPreferencesUtil.saveData(CoreApplication.getInstance().getApplicationContext(), "deviceId", deviceId);
    }

    /**
     * 设置userId
     */
    public static void setUserId(String userId) {
        SharedPreferencesUtil.saveData(CoreApplication.getInstance().getApplicationContext(), "userId", userId);
    }

    /**
     * 设置用户密码 password
     */
    public static void setPassword(String password) {
        SharedPreferencesUtil.saveData(CoreApplication.getInstance().getApplicationContext(), "password", password);
    }

    /**
     * 设置Token
     */
    public static void setToken(String token) {
        SharedPreferencesUtil.saveData(CoreApplication.getInstance().getApplicationContext(), "token", token);
    }

    /**
     * 设置phone
     */
    public static void setPhone(String phone) {
        SharedPreferencesUtil.saveData(CoreApplication.getInstance().getApplicationContext(), "phone", phone);
    }

    /**
     * 设置选择的城市adCode(默认应为定位城市)
     */
    public static void setChooseCityAdCode(String adCode) {
        SharedPreferencesUtil.saveData(CoreApplication.getInstance().getApplicationContext(), "chooseCityAdCode", adCode);
    }

    /**
     * 设置选择的城市cityCode(默认应为定位城市)
     */
    public static void setChooseCityCode(String cityCode) {
        SharedPreferencesUtil.saveData(CoreApplication.getInstance().getApplicationContext(), "chooseCityCode", cityCode);
    }

    /**
     * 设置选择的城市名称(默认应为定位城市)
     */
    public static void setChooseCityName(String cityName) {
        SharedPreferencesUtil.saveData(CoreApplication.getInstance().getApplicationContext(), "chooseCityName", cityName);
    }

    /**
     * 设置用户昵称
     */
    public static void setNickName(String nickName) {
        SharedPreferencesUtil.saveData(CoreApplication.getInstance().getApplicationContext(), "nickName", nickName);
    }

    /**
     * 设置用户头像
     */
    public static void setHeadUrl(String url) {
        SharedPreferencesUtil.saveData(CoreApplication.getInstance().getApplicationContext(), "headUrl", url);
    }

    /**
     * 设置消息提醒
     */
    public static void setMessageRemind(String isRemind) {
        SharedPreferencesUtil.saveData(CoreApplication.getInstance().getApplicationContext(), "messageRemind", isRemind);
    }

    /**
     * 设置消息提醒未读数
     */
    public static void setMessageUnread(int num) {
        SharedPreferencesUtil.saveData(CoreApplication.getInstance().getApplicationContext(), "messageUnread", num);
    }

    /**
     * 是否刚刚登录
     */
    public static void setIsLogin(int isLogin) {
        SharedPreferencesUtil.saveData(CoreApplication.getInstance().getApplicationContext(), "isLogin", isLogin);
    }

    /**
     * 是否刚刚注册
     */
    public static void setIsRegister(int isRegister) {
        SharedPreferencesUtil.saveData(CoreApplication.getInstance().getApplicationContext(), "isRegister", isRegister);
    }

    /**
     * 是否显示首页优惠券弹框
     */
    public static void setIsFlag(boolean isFlag) {
        SharedPreferencesUtil.saveData(CoreApplication.getInstance().getApplicationContext(), "isFlag", isFlag);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 获取是否接受推送消息
     */
    public static boolean getMessageReceive() {
        return (boolean) SharedPreferencesUtil.getData(CoreApplication.getInstance().getApplicationContext(), "isMessageReceive", true);
    }

    /**
     * 阿里云推送设备号 deviceId
     */
    public static String getDeviceId() {
        return String.valueOf(SharedPreferencesUtil.getData(CoreApplication.getInstance().getApplicationContext(), "deviceId", ""));
    }

    /**
     * 获取登录人的 userId
     */
    public static String getUserId() {
        return String.valueOf(SharedPreferencesUtil.getData(CoreApplication.getInstance().getApplicationContext(), "userId", ""));
    }

    /**
     * 获取用户密码 password
     */
    public static String getPassword() {
        return String.valueOf(SharedPreferencesUtil.getData(CoreApplication.getInstance().getApplicationContext(), "password", ""));
    }

    /**
     * 获取登录人的 Token
     */
    public static String getToken() {
        return String.valueOf(SharedPreferencesUtil.getData(CoreApplication.getInstance().getApplicationContext(), "token", ""));
    }

    /**
     * 获取登录人的 phone
     */
    public static String getPhone() {
        return String.valueOf(SharedPreferencesUtil.getData(CoreApplication.getInstance().getApplicationContext(), "phone", ""));
    }

    /**
     * 获取选择的城市adCode(默认应为定位城市)
     */
    public static String getChooseCityAdCode() {
        return String.valueOf(SharedPreferencesUtil.getData(CoreApplication.getInstance().getApplicationContext(), "chooseCityAdCode", ""));
    }

    /**
     * 获取选择的城市cityCode(默认应为定位城市)
     */
    public static String getChooseCityCode() {
        return String.valueOf(SharedPreferencesUtil.getData(CoreApplication.getInstance().getApplicationContext(), "chooseCityCode", ""));
    }

    /**
     * 获取选择的城市名称(默认应为定位城市)
     */
    public static String getChooseCityName() {
        return String.valueOf(SharedPreferencesUtil.getData(CoreApplication.getInstance().getApplicationContext(), "chooseCityName", ""));
    }

    /**
     * 获取用户名昵称
     */
    public static String getNickName() {
        return String.valueOf(SharedPreferencesUtil.getData(CoreApplication.getInstance().getApplicationContext(), "nickName", ""));
    }

    /**
     * 获取用户头像
     */
    public static String getHeadUrl() {
        return String.valueOf(SharedPreferencesUtil.getData(CoreApplication.getInstance().getApplicationContext(), "headUrl", ""));
    }

    /**
     * 获取消息提醒
     */
    public static String getMessageRemind() {
        return String.valueOf(SharedPreferencesUtil.getData(CoreApplication.getInstance().getApplicationContext(), "messageRemind", "1"));
    }

    /**
     * 获取消息提醒未读数
     */
    public static int getMessageUnread() {
        return (int) SharedPreferencesUtil.getData(CoreApplication.getInstance().getApplicationContext(), "messageUnread", 0);
    }

    /**
     * 是否刚刚登录
     */
    public static int getIsLogin() {
        return (int) SharedPreferencesUtil.getData(CoreApplication.getInstance().getApplicationContext(), "isLogin", 0);
    }

    /**
     * 是否刚刚注册
     */
    public static int getIsRegister() {
        return (int) SharedPreferencesUtil.getData(CoreApplication.getInstance().getApplicationContext(), "isRegister", 0);
    }

    /**
     * 是否显示首页优惠券弹框
     */
    public static boolean getIsFlag() {
        return (boolean) SharedPreferencesUtil.getData(CoreApplication.getInstance().getApplicationContext(), "isFlag", false);
    }
}
