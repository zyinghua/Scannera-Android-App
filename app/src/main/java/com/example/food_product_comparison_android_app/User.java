package com.example.food_product_comparison_android_app;

import com.facebook.AccessToken;

public class User {
    private AccessToken fb_access_token;
    private String id;
    private String username;
    private String first_name;
    private String last_name;
    private String email;
    private String profile_img_url;

    public User(String id, String username, String first_name, String last_name, String email, String profile_img_url) {
        this.fb_access_token = null;
        this.id = id;
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.profile_img_url = profile_img_url;
    }

    public User(AccessToken fb_access_token) {
        String s = "Facebook User";

        this.fb_access_token = fb_access_token;
        this.id = this.username = this.first_name = this.last_name = this.email = s;
        this.profile_img_url = null;
    }

    public AccessToken getFb_access_token() {
        return fb_access_token;
    }

    public void setFb_access_token(AccessToken fb_access_token) {
        this.fb_access_token = fb_access_token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_img_url() {
        return profile_img_url;
    }

    public void setProfile_img_url(String profile_img_url) {
        this.profile_img_url = profile_img_url;
    }
}
