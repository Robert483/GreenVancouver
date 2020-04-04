package com.parasinos.greenvancouver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Field {
    @SerializedName("category1")
    @Expose
    private String category1 = null;

    @SerializedName("category2")
    @Expose
    private String category2 = null;

    @SerializedName("name")
    @Expose
    private String name = null;

    @SerializedName("url")
    @Expose
    private String url = null;

    @SuppressWarnings("unused")
    @SerializedName("url2")
    @Expose
    private String url2 = null;

    @SuppressWarnings("unused")
    @SerializedName("url3")
    @Expose
    private String url3 = null;

    @SerializedName("mapid")
    @Expose
    private String mapID = null;

    @SerializedName("geom")
    @Expose
    private Geom geom = null;

    @SerializedName("address")
    @Expose
    private String address = null;

    @SerializedName("short_description")
    @Expose
    private String shortDescription = null;

    @SuppressWarnings("unused")
    @SerializedName("geo_local_area")
    @Expose
    private String geoLocalArea = null;

    public String getCategory1() {
        return category1;
    }

    public String getCategory2() {
        return category2;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getMapID() {
        return mapID;
    }

    public Geom getGeom() {
        return geom;
    }

    public String getAddress() {
        return address;
    }

    public String getShortDescription() {
        return shortDescription;
    }
}
