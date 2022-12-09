package com.xlyj.wood.domain;

import android.telecom.Call;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/31
 * fun:商品类
 */
public class Commodity extends BmobObject {
    private String name; //商品名称
    private Double price; //商品价格
    private String[] pictures;//图片
    private String[] details; //详细设计

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String[] getPictures() {
        return pictures;
    }

    public void setPictures(String[] pictures) {
        this.pictures = pictures;
    }

    public String[] getDetails() {
        return details;
    }

    public void setDetails(String[] details) {
        this.details = details;
    }
}
