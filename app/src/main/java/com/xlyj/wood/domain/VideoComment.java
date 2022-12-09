package com.xlyj.wood.domain;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/8
 * fun:
 */
public class VideoComment extends BmobObject {
    User user;
    Video video;
    BmobDate date;
    BmobRelation replays;
    String comment;

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

    public BmobDate getDate() {
        return date;
    }

    public void setDate(BmobDate date) {
        this.date = date;
    }

    public BmobRelation getReplays() {
        return replays;
    }

    public void setReplays(BmobRelation replays) {
        this.replays = replays;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
