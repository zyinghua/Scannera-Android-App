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
    String USER_ID_SERVER = "user_id";
    String USERNAME_SERVER = "username";
    String FIRSTNAME_SERVER = "firstname";
    String LASTNAME_SERVER = "lastname";
    String EMAIL_SERVER = "email";
    String PASSWORD_SERVER = "password";
    String PIMG_URL_SERVER = "pimg_url";

    @FormUrlEncoded
    @POST("api/user/add")
    Call<User> postUser(
            @Field(USERNAME_SERVER) String username,
            @Field(FIRSTNAME_SERVER) String firstname,
            @Field(LASTNAME_SERVER) String lastname,
            @Field(EMAIL_SERVER) String email,
            @Field(PASSWORD_SERVER) String password,
            @Field(PIMG_URL_SERVER) String profile_img_url
    );

    @FormUrlEncoded
    @PUT("api/user/update")
    Call<Void> updateUserInfoById(
            @Field(USER_ID_SERVER) String userId,
            @Field(PASSWORD_SERVER) String password
    );

    @GET("api/user/getBy/{acc_title}")
    Call<User> getUserByEmailOrUsername(@Path("acc_title") String acc_title);

    @GET("api/user/getByUsername/{username}")
    Call<User> getUserByUsername(@Path(USERNAME_SERVER) String username);

    @DELETE("api/user/delete/{user_id}")
    Call<Void> deleteUserById(@Path(USER_ID_SERVER) String user_id);

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
