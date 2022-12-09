package com.xlyj.wood.domain;

import cn.bmob.v3.BmobObject;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/8
 * fun:
 */
public class VideoReplay extends BmobObject {
    User user;
    VideoComment comment;
    String replay;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public VideoComment getComment() {
        return comment;
    }

    public void setComment(VideoComment comment) {
        this.comment = comment;
    }

    public String getReplay() {
        return replay;
    }

    public void setReplay(String replay) {
        this.replay = replay;
    }
}
