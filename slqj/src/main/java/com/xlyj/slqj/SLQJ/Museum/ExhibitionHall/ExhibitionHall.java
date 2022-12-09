package com.xlyj.slqj.SLQJ.Museum.ExhibitionHall;

public class ExhibitionHall {
    private String name;
    private int imageId;
    private String location;
    public ExhibitionHall(String name, int imageId,String location) {
        this.name = name;
        this.imageId = imageId;
        this.location=location;
    }
    public String getName() {
        return name;
    }
    public int getImageId() {
        return imageId;
    }
    public String getLocation(){
        return location;
    }

}
