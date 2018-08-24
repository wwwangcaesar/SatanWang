package com.lovcreate.greendao.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/3/13.
 */
@Entity
public class HomeActivity {
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String aid;
    private String isShow;
    @Generated(hash = 1222144721)
    public HomeActivity(Long id, String name, String aid, String isShow) {
        this.id = id;
        this.name = name;
        this.aid = aid;
        this.isShow = isShow;
    }
    @Generated(hash = 1581733803)
    public HomeActivity() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAid() {
        return this.aid;
    }
    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }
}
