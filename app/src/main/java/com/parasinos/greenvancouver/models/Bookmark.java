package com.parasinos.greenvancouver.models;

import android.graphics.drawable.Drawable;

import com.google.firebase.database.Exclude;

public class Bookmark {
    private String address;
    private String name;
    private String mapid;

    @Exclude
    private Drawable image;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMapid() {
        return mapid;
    }

    public void setMapid(String mapid) {
        this.mapid = mapid;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}
