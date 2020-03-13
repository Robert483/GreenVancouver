package com.parasinos.greenvancouver;

import java.util.ArrayList;

public class User {
    public String name;
    public String profilePicture;
    public ArrayList<String> bookmarks;
    public ArrayList<String> reviews;

    public User(String name, String profilePicture) {
        this.name = name;
        this.profilePicture = profilePicture;
        bookmarks = null;
        reviews = null;
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

    public ArrayList<String> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(ArrayList<String> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public ArrayList<String> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<String> reviews) {
        this.reviews = reviews;
    }
}
