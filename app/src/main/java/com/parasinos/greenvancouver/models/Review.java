package com.parasinos.greenvancouver.models;

import com.google.firebase.database.Exclude;

import androidx.annotation.Nullable;

public class Review {
    @Exclude
    private String key;
    private Integer rating;
    private String content;
    private String name;
    private String profilePicture;
    private String projectName;

    public Review() {
    }

    public Review(String key) {
        this.key = key;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Review)) {
            return false;
        }

        return ((Review) obj).key.equals(key);
    }
}
