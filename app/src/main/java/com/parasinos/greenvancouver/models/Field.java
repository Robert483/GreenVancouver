package com.parasinos.greenvancouver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// MAPID;NAME;CATEGORY1;CATEGORY2;ADDRESS;SHORT_DESCRIPTION;URL;URL2;URL3;Geom;Geo Local Area
public class Field {
    @SerializedName("category1")
    @Expose
    private String category1 = null;

    public String getCategory1() {
        return category1;
    }

    @SerializedName("category2")
    @Expose
    private String category2 = null;

    public String getCategory2() {
        return category2;
    }

    @SerializedName("name")
    @Expose
    private String name = null;

    public String getName() {
        return name;
    }

    @SerializedName("url")
    @Expose
    private String url = null;

    public String getUrl() {
        return url;
    }
    @SerializedName("url2")
    @Expose
    private String url2 = null;

    public String getUrl2() {
        return url;
    }
    @SerializedName("url3")
    @Expose
    private String url3 = null;

    public String getUrl3() {
        return url;
    }

    @SerializedName("mapid")
    @Expose
    private String mapID = null;


    @SerializedName("geom")
    @Expose
    private Geom geom = null;

    public Geom getGeom() {
        return geom;
    }

    @SerializedName("address")
    @Expose
    private String address = null;

    public String getAddress() {
        return address;
    }

    @SerializedName("short_description")
    @Expose
    private String shortDescription = null;

    public String getShortDescription() {
        return shortDescription;
    }

    @SerializedName("geo_local_area")
    @Expose
    private String geoLocalArea = null;

    public String getGeoLocalArea() {
        return geoLocalArea;
    }
}
