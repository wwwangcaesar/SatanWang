package com.lovcreate.greendao.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Bright on 2018/3/15.
 */
@Entity
public class SearchHomeHistory {
    @Id
    private Long id;
    private String userId;
    private String historyContent;
    @Property(nameInDb = "create_time")
    private Date createTime;
    @Generated(hash = 983679051)
    public SearchHomeHistory(Long id, String userId, String historyContent,
            Date createTime) {
        this.id = id;
        this.userId = userId;
        this.historyContent = historyContent;
        this.createTime = createTime;
    }
    @Generated(hash = 1062237951)
    public SearchHomeHistory() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getHistoryContent() {
        return this.historyContent;
    }
    public void setHistoryContent(String historyContent) {
        this.historyContent = historyContent;
    }
    public Date getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
