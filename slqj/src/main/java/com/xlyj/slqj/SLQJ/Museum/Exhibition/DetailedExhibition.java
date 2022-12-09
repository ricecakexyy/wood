package com.xlyj.slqj.SLQJ.Museum.Exhibition;

public class DetailedExhibition {
    private String name,loc_f,loc_hall;
    private int imageId,looks;

    public DetailedExhibition(String name, String loc_f, String loc_hall, int imageId, int looks) {
        this.name = name;
        this.loc_f = loc_f;
        this.loc_hall = loc_hall;
        this.imageId = imageId;
        this.looks = looks;
    }

    public String getName() {
        return name;
    }

    public String getLoc_f() {
        return loc_f;
    }

    public String getLoc_hall() {
        return loc_hall;
    }

    public int getImageId() {
        return imageId;
    }

    public int getLooks() {
        return looks;
    }
}
