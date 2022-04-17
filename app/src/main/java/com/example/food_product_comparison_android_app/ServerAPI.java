package com.example.food_product_comparison_android_app;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

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
import retrofit2.http.Query;

public interface ServerAPI {
    String UPDATE_USER_END_POINT = "api/user/update";
    String USER_ID_SERVER = "user_id";
    String USERNAME_SERVER = "username";
    String FIRSTNAME_SERVER = "firstname";
    String LASTNAME_SERVER = "lastname";
    String EMAIL_SERVER = "email";
    String PASSWORD_SERVER = "password";
    String PIMG_URL_SERVER = "pimg_url";
    String FEEDBACK_RATING_SERVER = "feedback_rating";
    String FEEDBACK_DESC_SERVER = "feedback_description";
    String PRODUCT_ID_SERVER = "product_id";
    String PRODUCT_BARCODE_SERVER = "product_barcode";
    String PRODUCT_BRAND_SERVER = "product_brand";
    String PRODUCT_CATEGORY_SERVER = "product_cate";
    String PRODUCT_NAME_SERVER = "product_name";
    String PRODUCT_PRICE_SERVER = "product_price";
    String PRODUCT_NUTRITION_SERVER = "product_nutrition";
    String PRODUCT_IS_STARRED_SERVER = "product_is_starred";
    String PRODUCT_SCAN_DATE_SERVER = "product_scan_date";
    DateFormat DATE_FORMAT_SERVER = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.ENGLISH);
    String GET_STARRED_PRODUCTS_SERVER = "api/favourite/get";

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
    @PUT(UPDATE_USER_END_POINT)
    Call<Void> updateUserPasswordById(
            @Field(USER_ID_SERVER) String userId,
            @Field(PASSWORD_SERVER) String password
    );

    @GET("api/user/getBy/{acc_title}")
    Call<User> getUserByEmailOrUsername(@Path("acc_title") String acc_title);

    @DELETE("api/user/delete/{user_id}")
    Call<Void> deleteUserById(@Path(USER_ID_SERVER) String user_id);

    @FormUrlEncoded
    @PUT(UPDATE_USER_END_POINT)
    Call<Void> updateUsernameById(
            @Field(USER_ID_SERVER) String userId,
            @Field(USERNAME_SERVER) String username
    );

    @FormUrlEncoded
    @PUT(UPDATE_USER_END_POINT)
    Call<Void> updateUserFirstnameById(
            @Field(USER_ID_SERVER) String userId,
            @Field(FIRSTNAME_SERVER) String firstname
    );

    @FormUrlEncoded
    @PUT(UPDATE_USER_END_POINT)
    Call<Void> updateUserLastnameById(
            @Field(USER_ID_SERVER) String userId,
            @Field(LASTNAME_SERVER) String lastname
    );

    @FormUrlEncoded
    @POST("api/feedback/new")
    Call<Void> postFeedback(
            @Field(USER_ID_SERVER) String user_id,
            @Field(FEEDBACK_RATING_SERVER) Float rating,
            @Field(FEEDBACK_DESC_SERVER) String description
    );

    @GET(GET_STARRED_PRODUCTS_SERVER)
    Call<List<Product>> getStarredProducts(
            @Query(USER_ID_SERVER) String user_id
    );

    @FormUrlEncoded
    @POST("api/favourite/add")
    Call<Void> starProduct(
            @Field(USER_ID_SERVER) String user_id,
            @Field(PRODUCT_ID_SERVER) String product_id
    );

    @FormUrlEncoded
    @POST("api/favourite/remove")
    Call<Void> unStarProduct(
            @Field(USER_ID_SERVER) String user_id,
            @Field(PRODUCT_ID_SERVER) String product_id
    );

    @GET("api/favourite/get")
    Call<List<Product>> getRecommendedProducts(
            @Query(USER_ID_SERVER) String user_id
    );
}
