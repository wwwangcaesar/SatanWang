package com.lovcreate.greendao.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Administrator on 2018/3/13.
 */
@Entity
public class NotLoginCouponListBean {
    @Id(autoincrement = true)
    private Long id;
    private String aid;

    @Generated(hash = 1951072091)
    public NotLoginCouponListBean(Long id, String aid) {
        this.id = id;
        this.aid = aid;
    }

    @Generated(hash = 443975395)
    public NotLoginCouponListBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAid() {
        return this.aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }
}
