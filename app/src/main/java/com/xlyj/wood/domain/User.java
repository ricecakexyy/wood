package com.xlyj.wood.domain;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/1
 * fun:
 */
public class User extends BmobUser {
    private String nickname; //昵称
    private String avatar; //头像
    private BmobRelation activities;
    private BmobRelation shopCart;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickName) {
        this.nickname = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public BmobRelation getActivities() {
        return activities;
    }

    public void setActivities(BmobRelation activities) {
        this.activities = activities;
    }

    public BmobRelation getShopCart() {
        return shopCart;
    }

    public void setShopCart(BmobRelation shopCart) {
        this.shopCart = shopCart;
    }
}
