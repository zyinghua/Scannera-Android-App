package com.example.food_product_comparison_android_app;

public class ProductReview {
    private String product_id;
    private String user_id;
    private float rating;
    private String description;

    public ProductReview(String product_id, String user_id, float rating, String description) {
        this.product_id = product_id;
        this.user_id = user_id;
        this.rating = rating;
        this.description = description;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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
