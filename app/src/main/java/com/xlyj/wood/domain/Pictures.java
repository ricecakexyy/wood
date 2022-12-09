package com.xlyj.wood.domain;

import cn.bmob.v3.BmobObject;

/**
 * @author cute xyy biu~biu~
 * created on 2020/9/13
 * fun:
 */
public class Pictures extends BmobObject {
    private String pic_type;
    private String name;
    private String url;

    public String getPic_type() {
        return pic_type;
    }

    public void setPic_type(String pic_type) {
        this.pic_type = pic_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
