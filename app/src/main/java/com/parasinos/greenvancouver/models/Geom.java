package com.parasinos.greenvancouver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Geom {
    @SerializedName("type")
    @Expose
    private String geomType = null;

    public String getType() {
        return geomType;
    }

    @SerializedName("coordinates")
    @Expose
    private double[] coordinates = null;

    public double[] getCoordinates() {
        return coordinates;
    }
}

