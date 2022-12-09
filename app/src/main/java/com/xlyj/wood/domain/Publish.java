package com.xlyj.wood.domain;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobPointer;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/7
 * fun: 动态
 */
public class Publish extends BmobObject {
    User user;
    BmobDate date;
    String[] pictures;
    String content;
    Integer comment_cnt, like_cnt, forward_cnt;
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

    public String[] getPictures() {
        return pictures;
    }

    public void setPictures(String[] pictures) {
        this.pictures = pictures;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getComment_cnt() {
        return comment_cnt;
    }

    public void setComment_cnt(Integer comment_cnt) {
        this.comment_cnt = comment_cnt;
    }

    public Integer getLike_cnt() {
        return like_cnt;
    }

    public void setLike_cnt(Integer like_cnt) {
        this.like_cnt = like_cnt;
    }

    public Integer getForward_cnt() {
        return forward_cnt;
    }

    public void setForward_cnt(Integer forward_cnt) {
        this.forward_cnt = forward_cnt;
    }
}
