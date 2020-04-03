package com.parasinos.greenvancouver;

public class Bookmark {
    public String address;
    public String name;
    public String mapid;

    public Bookmark(String address, String name, String mapid) {
        this.address = address;
        this.name = name;
        this.mapid = mapid;
    }

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

    public String getMapid() { return mapid; }

    public void setMapid(String mapid) { this.mapid = mapid; }
}
