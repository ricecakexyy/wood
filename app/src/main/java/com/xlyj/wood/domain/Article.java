package com.xlyj.wood.domain;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/18
 * fun: 文章类
 */
public class Article extends BmobObject {
    private String html;
    private User user;
    private BmobDate date;
    private String title;
    private String cover;
    private String des;

    private Integer like_cnt, comment_cnt;

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BmobDate getDate() {
        return date;
    }

    public void setDate(BmobDate date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Integer getLike_cnt() {
        return like_cnt;
    }

    public void setLike_cnt(Integer like_cnt) {
        this.like_cnt = like_cnt;
    }

    public Integer getComment_cnt() {
        return comment_cnt;
    }

    public void setComment_cnt(Integer comment_cnt) {
        this.comment_cnt = comment_cnt;
    }
}
