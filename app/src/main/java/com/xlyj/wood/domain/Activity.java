package com.xlyj.wood.domain;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/15
 * fun:
 */
public class Activity extends BmobObject {
    private String cover;
    private User user;
    private String name;
    private String details;
    private BmobDate dateStart;
    private BmobDate dateEnd;
    private Integer needNum;
    private BmobRelation applications;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public BmobDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(BmobDate dateStart) {
        this.dateStart = dateStart;
    }

    public BmobDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(BmobDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Integer getNeedNum() {
        return needNum;
    }

    public void setNeedNum(Integer needNum) {
        this.needNum = needNum;
    }

    public BmobRelation getApplications() {
        return applications;
    }

    public void setApplications(BmobRelation applications) {
        this.applications = applications;
    }
}
