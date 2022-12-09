package com.xlyj.slqj.SLQJ.Museum.Exhibition;

public class Exhibition {
    private String name;
    private int imageId;

    public Exhibition(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }
    public String getName() {
        return name;
    }
    public int getImageId() {
        return imageId;
    }

}
