package com.example.food_product_comparison_android_app;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ServerAPI {
    @FormUrlEncoded
    @POST("api/user/add")
    Call<Void> createUser(
            @Field("username") String username,
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("email") String email,
            @Field("password") String password,
            @Field("pimg_url") String profile_img_url
    );

    @PATCH("api/user/patchbyEmail/{email}")
    Call<Void> patchUserPasswordByEmail(@Path("email") String email);

    @GET("api/user/getbyEmail/{email}")
    Call<User> getUserByEmail(@Path("email") String email);
}
