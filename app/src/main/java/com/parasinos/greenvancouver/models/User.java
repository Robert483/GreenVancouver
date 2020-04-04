package com.parasinos.greenvancouver.models;

public class User {
    private String name;
    private String profilePicture;

    @SuppressWarnings("unused")
    public User() {
    }

    public User(String name, String profilePicture) {
        this.name = name;
        this.profilePicture = profilePicture;
    }

    public String getName() {
        return name;
    }

    public String getProfilePicture() {
        return profilePicture;
    }
}
