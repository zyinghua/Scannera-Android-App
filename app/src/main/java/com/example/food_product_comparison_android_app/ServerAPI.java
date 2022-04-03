package com.example.food_product_comparison_android_app;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface ServerAPI {
    @FormUrlEncoded
    @POST("api/user/add")
    Call<Void> createUser(
            @Field("fname") String firstname,
            @Field("userName") String username,
            @Field("lname") String lastname,
            @Field("email") String email,
            @Field("password") String password
    );

    @PATCH
    Call<Void> patchUserPassword(

    );

    @GET
    Call<String> getUserPassword();

    @GET
    Call<User> getUserByEmail();
}
