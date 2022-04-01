package com.example.food_product_comparison_android_app;

import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServerAPI {

    @POST
    void createUser(@Body User user);
}
