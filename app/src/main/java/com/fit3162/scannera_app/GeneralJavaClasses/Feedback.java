package com.fit3162.scannera_app.GeneralJavaClasses;

import com.fit3162.scannera_app.ServerRetrofitAPI;
import com.google.gson.annotations.SerializedName;

public class Feedback {
    @SerializedName(ServerRetrofitAPI.USER_ID_SERVER)
    private String userId;

    @SerializedName(ServerRetrofitAPI.FEEDBACK_RATING_SERVER)
    private float rating;

    @SerializedName(ServerRetrofitAPI.FEEDBACK_DESC_SERVER)
    private String description;

    public Feedback(String userId, float rating, String description) {
        this.userId = userId;
        this.rating = rating;
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
