package com.example.food_product_comparison_android_app;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServerAPI {
    @FormUrlEncoded
    @POST("api/user/add")
    Call<User> postUser(
            @Field("username") String username,
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("email") String email,
            @Field("password") String password,
            @Field("pimg_url") String profile_img_url
    );

    @FormUrlEncoded
    @PUT("api/user/update")
    Call<Void> updateUserPasswordById(
            @Field("user_id") String userId,
            @Field("password") String password
    );

    @GET("api/user/getBy/{acc_info}")
    Call<User> getUserByEmailOrUsername(@Path("acc_info") String acc_info);

    @GET("api/user/getByUsername/{username}")
    Call<User> getUserByUsername(@Path("username") String username);

    @DELETE("api/user/delete/{user_id}")
    Call<Void> deleteUserById(@Path("user_id") String user_id);

    @FormUrlEncoded
    @PUT("api/user/update")
    Call<Void> updateUsernameById(
            @Field("user_id") String userId,
            @Field("username") String username
    );

    @FormUrlEncoded
    @PUT("api/user/update")
    Call<Void> updateUserFirstnameById(
            @Field("user_id") String userId,
            @Field("firstname") String firstname
    );

    @FormUrlEncoded
    @PUT("api/user/update")
    Call<Void> updateUserLastnameById(
            @Field("user_id") String userId,
            @Field("lastname") String lastname
    );

    @POST("api/")
    Call<Void> postFeedback(@Body Feedback feedback);
}
