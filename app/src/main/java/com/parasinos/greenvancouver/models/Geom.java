package com.parasinos.greenvancouver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Geom {
    @SuppressWarnings("unused")
    @SerializedName("type")
    @Expose
    private String geomType = null;

    @SerializedName("coordinates")
    @Expose
    private double[] coordinates = null;

    public double[] getCoordinates() {
        return coordinates;
    }
}
