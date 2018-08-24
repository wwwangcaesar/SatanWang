/*
  Copyright (c) 2017, lovcreate.com. All rights reserved.
*/
package com.lovcreate.push.bean;


/**
 * Created by DuChuanLei on 2017/7/31
 */
public class PushMessageResponse {
    private String pushType;
    private String deviceIds;
    private String title;
    private String sortId;
    private String id;
    private String releaseTime;
    private String url;
    private String releaseTimeForApp;
    /**
     * 收益类型
     */
    private String profitType;
    private String _ALIYUN_NOTIFICATION_ID_;

    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    public String getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(String deviceIds) {
        this.deviceIds = deviceIds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSortId() {
        return sortId;
    }

    public void setSortId(String sortId) {
        this.sortId = sortId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getReleaseTimeForApp() {
        return releaseTimeForApp;
    }

    public void setReleaseTimeForApp(String releaseTimeForApp) {
        this.releaseTimeForApp = releaseTimeForApp;
    }

    public String getProfitType() {
        return profitType;
    }

    public void setProfitType(String profitType) {
        this.profitType = profitType;
    }

    public String get_ALIYUN_NOTIFICATION_ID_() {
        return _ALIYUN_NOTIFICATION_ID_;
    }

    public void set_ALIYUN_NOTIFICATION_ID_(String _ALIYUN_NOTIFICATION_ID_) {
        this._ALIYUN_NOTIFICATION_ID_ = _ALIYUN_NOTIFICATION_ID_;
    }
}
