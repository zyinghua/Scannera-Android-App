package com.example.food_product_comparison_android_app;

import com.facebook.AccessToken;

import java.util.ArrayList;

public class User {
    private int login_flag;
    private String id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String profile_img_url;
    private AccessToken fb_access_token;

    public User(int login_flag, String id, String username, String firstname, String lastname, String email, String profile_img_url, String password) {
        // Local user
        this.login_flag = login_flag;
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.profile_img_url = profile_img_url;
        this.password = password;
    }

    public User(int login_flag, String username, String firstname, String lastname, String email, String password, String profile_img_url) {
        this.login_flag = login_flag;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.profile_img_url = profile_img_url;
    }

    public User(int login_flag, AccessToken fb_access_token) {
        // Facebook user
        String s = "<Facebook User>";

        this.login_flag = login_flag;
        this.fb_access_token = fb_access_token;
        this.id = this.username = this.firstname = this.lastname = this.email = s;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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

    public void updateUserInfo(String username, String firstname, String lastname, String email_address)
    {
        // Password should be updated directly as it won't be stored in the corresponding textview(s)
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email_address;
        this.password = password;
    }

    public int getLogin_flag() {
        return login_flag;
    }

    public void setLogin_flag(int login_flag) {
        this.login_flag = login_flag;
    }

}
