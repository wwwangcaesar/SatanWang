package com.lovcreate.greendao.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

import java.util.Date;

/**
 * Created by Bright on 2018/2/22.
 */
@Entity
public class SearchStationHistory {
    @Id
    private Long id;
    private String userId;
    private String historyContent;
    @Property(nameInDb = "create_time")
    private Date createTime;

    @Generated(hash = 565990527)
    public SearchStationHistory(Long id, String userId, String historyContent,
            Date createTime) {
        this.id = id;
        this.userId = userId;
        this.historyContent = historyContent;
        this.createTime = createTime;
    }

    @Generated(hash = 2113475849)
    public SearchStationHistory() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHistoryContent() {
        return historyContent;
    }

    public void setHistoryContent(String historyContent) {
        this.historyContent = historyContent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
