package com.parasinos.greenvancouver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import androidx.annotation.NonNull;

public class Project {
    @SerializedName("datasetid")
    @Expose
    private String dataSetID = null;

    public String getDataSetId() {
        return dataSetID;
    }

    @SerializedName("recordid")
    @Expose
    private String recordID = null;

    public String getRecordID() {
        return recordID;
    }

    @SerializedName("fields")
    @Expose
    private Field field = null;

    public Field getField() {
        return field;
    }

    @SerializedName("record_timestamp")
    @Expose
    private Date recordTimestamp = null;

    public Date getRecordTimestamp() {
        return recordTimestamp;
    }

    @NonNull
    @Override
    public String toString() {
        return this.field.getName();
    }


}
