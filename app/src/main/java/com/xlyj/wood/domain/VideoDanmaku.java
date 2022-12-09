package com.xlyj.wood.domain;

import cn.bmob.v3.BmobObject;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/13
 * fun: 视频弹幕类
 */
public class VideoDanmaku extends BmobObject {
    String text;
    Integer textSize;
    Integer textColor;
    Long time;
    User user;
    Video video;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getTextSize() {
        return textSize;
    }

    public void setTextSize(Integer textSize) {
        this.textSize = textSize;
    }

    public Integer getTextColor() {
        return textColor;
    }

    public void setTextColor(Integer textColor) {
        this.textColor = textColor;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }
}
