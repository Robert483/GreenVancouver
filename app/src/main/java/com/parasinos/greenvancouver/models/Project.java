package com.parasinos.greenvancouver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import androidx.annotation.NonNull;

public class Project {
    @SuppressWarnings("unused")
    @SerializedName("datasetid")
    @Expose
    private String dataSetID = null;

    @SuppressWarnings("unused")
    @SerializedName("recordid")
    @Expose
    private String recordID = null;

    @SerializedName("fields")
    @Expose
    private Field field = null;

    @SuppressWarnings("unused")
    @SerializedName("record_timestamp")
    @Expose
    private Date recordTimestamp = null;

    public Field getField() {
        return field;
    }

    @NonNull
    @Override
    public String toString() {
        return this.field.getName();
    }
}
