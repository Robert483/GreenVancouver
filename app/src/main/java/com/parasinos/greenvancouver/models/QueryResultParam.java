package com.parasinos.greenvancouver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class QueryResultParam {

    @SerializedName("dataset")
    @Expose
    private String dataSetName = null;

    public String getDataSetName() {
        return dataSetName;
    }

    @SerializedName("timezone")
    @Expose
    private String timezone = null;

    public String getTimezone() {
        return timezone;
    }

    @SerializedName("q")
    @Expose
    private String q = null;

    public String getQ() {
        return q;
    }

    @SerializedName("rows")
    @Expose
    private int rows = 0;

    public int getRows() {
        return rows;
    }

    @SerializedName("format")
    @Expose
    private String format = null;

    public String getFormat() {
        return format;
    }


}
