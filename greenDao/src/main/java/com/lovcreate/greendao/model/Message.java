package com.lovcreate.greendao.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Bright on 2018/2/6.
 */
@Entity
public class Message {
    @Id(autoincrement = true)
    private Long id;
    private String title;
    private String content;
    private String description;
    private String time;
    private boolean isRead;
    private String type;
    private String businessId;
    private String userId;
    @Generated(hash = 1287700098)
    public Message(Long id, String title, String content, String description, String time, boolean isRead,
            String type, String businessId, String userId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.description = description;
        this.time = time;
        this.isRead = isRead;
        this.type = type;
        this.businessId = businessId;
        this.userId = userId;
    }
    @Generated(hash = 637306882)
    public Message() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public boolean getIsRead() {
        return this.isRead;
    }
    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getBusinessId() {
        return this.businessId;
    }
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public double getDataSize() {
        double idSize = id != null ? String.valueOf(id).length() : 0;//一个字符1字节
        double typeSize = type != null ? type.length() : 0;//一个字符1字节
        double contentSize = content != null ? content.length() * 2 : 0;//按汉字算,一个字符2字节
        double titleSize = title != null ? title.length() * 2 : 0;//按汉字算,一个字符2字节
        double descriptionSize = description != null ? description.length() * 2 : 0;//按汉字算,一个字符2字节
        double businessIdSize = businessId != null ? businessId.length() * 2 : 0;//按汉字算,一个字符2字节
        double timeSize = time != null ? time.length() * 2 : 0;//按汉字算,一个字符2字节
        double userIdSize = userId != null ? userId.length() * 2 : 0;//按汉字算,一个字符2字节
        return idSize + typeSize + titleSize + contentSize + descriptionSize + businessIdSize + timeSize;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

}
