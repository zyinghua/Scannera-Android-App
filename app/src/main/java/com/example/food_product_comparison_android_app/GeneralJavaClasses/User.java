package com.example.food_product_comparison_android_app.GeneralJavaClasses;

import com.example.food_product_comparison_android_app.Utils;
import com.facebook.AccessToken;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class User {
    private int loginFlag;

    @SerializedName("user_id")
    private String id;

    @SerializedName("user_username")
    private String username;

    @SerializedName("user_firstname")
    private String firstname;

    @SerializedName("user_lastname")
    private String lastname;

    @SerializedName("user_email")
    private String email;

    @SerializedName("user_password")
    private String password;

    @SerializedName("user_pimg_url")
    private String profile_img_url;

    @SerializedName("user_contribution_score")
    private int contributionScore;

    public User(int login_flag, String username, String firstname, String lastname, String email, String password) {
        this.loginFlag = login_flag;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.contributionScore = 0;
    }

    public User(int login_flag, String username, String firstname, String lastname, String email, String password, int contribution_score) {
        this.loginFlag = login_flag;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.contributionScore = contribution_score;
    }

    public User(int login_flag, String username, String firstname, String lastname, String email, String password, String profile_img_url) {
        this.loginFlag = login_flag;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.profile_img_url = profile_img_url;
        this.contributionScore = 0;
    }

    public User(int login_flag, String username, String firstname, String lastname, String email, String password, String profile_img_url, int contribution_score) {
        this.loginFlag = login_flag;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.profile_img_url = profile_img_url;
        this.contributionScore = contribution_score;
    }

    public User() {
        // For error use, avoid app crushing
        this.loginFlag = Utils.LOCAL_LOGIN;
        this.id = "null";
        this.username = "null";
        this.firstname = "null";
        this.lastname = "null";
        this.email = "null";
        this.password = "null";
        this.profile_img_url = "null";
        this.contributionScore = 0;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public int getLoginFlag() {
        return loginFlag;
    }

    public void setLoginFlag(int loginFlag) {
        this.loginFlag = loginFlag;
    }

    public int getContributionScore() {
        return contributionScore;
    }

    public void setContributionScore(int contribution_score) {
        this.contributionScore = contribution_score;
    }
}
