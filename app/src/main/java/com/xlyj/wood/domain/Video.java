package com.xlyj.wood.domain;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/7
 * fun:视频类
 */
public class Video extends BmobObject {
    private User author; //视频作者
    private String title; //视频标题
    private String brief;//视频简介
    private BmobDate date; //视频发布日期
    private String video_url;//视频地址
    private String cover_url; //封面地址
    private Integer comment_cnt;//评论个数
    private Integer like_cnt;//喜欢个数
    private BmobRelation comments;//评论
    private BmobRelation danmakus; //弹幕

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public BmobDate getDate() {
        return date;
    }

    public void setDate(BmobDate date) {
        this.date = date;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public BmobRelation getComments() {
        return comments;
    }

    public void setComments(BmobRelation comments) {
        this.comments = comments;
    }

    public BmobRelation getDanmakus() {
        return danmakus;
    }

    public void setDanmakus(BmobRelation danmakus) {
        this.danmakus = danmakus;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
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
}
