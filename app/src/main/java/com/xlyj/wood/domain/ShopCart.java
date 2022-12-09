package com.xlyj.wood.domain;

import cn.bmob.v3.BmobObject;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/31
 * fun: 购物车类
 */
public class ShopCart extends BmobObject {
    private Commodity commodity;
    private Integer number;
    private User user;
    private boolean isCheck;

    public Commodity getCommodity() {
        return commodity;
    }

    public void setCommodity(Commodity commodity) {
        this.commodity = commodity;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
