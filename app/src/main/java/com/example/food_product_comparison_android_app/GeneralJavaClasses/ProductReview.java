package com.example.food_product_comparison_android_app.GeneralJavaClasses;

import com.google.gson.annotations.SerializedName;

public class ProductReview {
    private String username;
    private String userPImgUrl;
    private String date;
    private float rating;
    private String description;

    public ProductReview() {
    }

    public ProductReview(String username, String userPImgUrl, String date, float rating, String description) {
        this.username = username;
        this.userPImgUrl = userPImgUrl;
        this.date = date;
        this.rating = rating;
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPImgUrl() {
        return userPImgUrl;
    }

    public void setUserPImgUrl(String userPImgUrl) {
        this.userPImgUrl = userPImgUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
