package com.parasinos.greenvancouver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class QueryResultParam {
    @SuppressWarnings("unused")
    @SerializedName("dataset")
    @Expose
    private String dataSetName = null;

    @SuppressWarnings("unused")
    @SerializedName("timezone")
    @Expose
    private String timezone = null;

    @SuppressWarnings("unused")
    @SerializedName("q")
    @Expose
    private String q = null;

    @SuppressWarnings("unused")
    @SerializedName("rows")
    @Expose
    private int rows = 0;

    @SuppressWarnings("unused")
    @SerializedName("format")
    @Expose
    private String format = null;
}
