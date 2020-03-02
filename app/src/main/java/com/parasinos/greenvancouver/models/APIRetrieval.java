package com.parasinos.greenvancouver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class APIRetrieval {
    @SerializedName("nhits")
    @Expose
    private int numHits = 0;

    public int getNumHits(){
        return numHits;
    }

    @SerializedName("parameters")
    @Expose
    private QueryResultParam param = null;

    @SerializedName("records")
    @Expose
    private List<Project> records = null;
    public List<Project> getRecords() {
        return records;
    }

}

