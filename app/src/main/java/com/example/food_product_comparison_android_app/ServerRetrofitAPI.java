package com.example.food_product_comparison_android_app;

import com.example.food_product_comparison_android_app.GeneralJavaClasses.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServerRetrofitAPI {
    String USER_END_POINT_SERVER = "api/user/";
    String UPDATE_USER_END_POINT = USER_END_POINT_SERVER + "update";
    String ADD_USER_END_POINT = USER_END_POINT_SERVER + "add";
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
    String PRODUCT_SCAN_DATE_SERVER = "product_scan_timestamp";
    String PRODUCT_IMG_URL_SERVER = "product_display_img";
    String PRODUCT_NUTRITION_PIC_SERVER = "nutrition_img"; // For sending the nutrition img to the server, the key on the server side
    String PRODUCT_DISPLAY_PIC_SERVER = "display_img"; // For sending the product img to the server, the key on the server side
    DateFormat DATE_FORMAT_SERVER = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.ENGLISH);
    String PRODUCT_END_POINT_SERVER = "api/product/";
    String POST_PRODUCT_SERVER = PRODUCT_END_POINT_SERVER + "new";
    String GET_PRODUCT_SERVER = PRODUCT_END_POINT_SERVER + "get/";
    String STARRED_PRODUCTS_SERVER = "api/favourite/";
    String GET_STARRED_PRODUCTS_SERVER = STARRED_PRODUCTS_SERVER + "get";
    String ADD_STARRED_PRODUCT_SERVER = STARRED_PRODUCTS_SERVER + "add";
    String REMOVE_STARRED_PRODUCT_SERVER = STARRED_PRODUCTS_SERVER + "remove";
    String ADD_SCANNED_PRODUCT_SERVER = "api/scan/add";
    String GET_SCANNED_PRODUCTS_SERVER = "api/scan/get";
    String PRODUCT_REVIEW_END_POINT = "api/review/";
    String GET_PRODUCT_REVIEWS_SERVER = PRODUCT_REVIEW_END_POINT + "get";
    String ADD_PRODUCT_REVIEW_SERVER = PRODUCT_REVIEW_END_POINT + "new";
    String GET_SIMILAR_PRODUCTS_SERVER = "api/product/similar/";
    String PRODUCT_REVIEW_RATING_SERVER = "review_rating";
    String PRODUCT_REVIEW_DESC_SERVER = "review_description";
    String PRODUCT_REVIEW_DATE_SERVER = "review_date";

    @FormUrlEncoded
    @POST(ADD_USER_END_POINT)
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
            @Field(FEEDBACK_RATING_SERVER) float rating,
            @Field(FEEDBACK_DESC_SERVER) String description
    );

    @Multipart
    @POST(POST_PRODUCT_SERVER)
    Call<ResponseBody> postProduct(
            @Part(USER_ID_SERVER) RequestBody user_id,
            @Part(PRODUCT_BARCODE_SERVER) RequestBody product_barcode,
            @Part(PRODUCT_BRAND_SERVER) RequestBody product_brand,
            @Part(PRODUCT_NAME_SERVER) RequestBody product_name,
            @Part(PRODUCT_PRICE_SERVER) RequestBody product_price,
            @Part(PRODUCT_CATEGORY_SERVER) RequestBody product_category,
            @Part MultipartBody.Part nutrition_pic,
            @Part MultipartBody.Part product_pic
    );

    @GET(GET_PRODUCT_SERVER + "{product_barcode}")
    Call<ResponseBody> getASingleProduct(
            @Path("product_barcode") String product_barcode,
            @Query(USER_ID_SERVER) String user_id
    );

    @GET(GET_STARRED_PRODUCTS_SERVER)
    Call<ResponseBody> getStarredProducts(
            @Query(USER_ID_SERVER) String user_id
    );

    @FormUrlEncoded
    @POST(ADD_STARRED_PRODUCT_SERVER)
    Call<Void> starProduct(
            @Field(USER_ID_SERVER) String user_id,
            @Field(PRODUCT_ID_SERVER) String product_id
    );

    @FormUrlEncoded
    @POST(REMOVE_STARRED_PRODUCT_SERVER)
    Call<Void> unStarProduct(
            @Field(USER_ID_SERVER) String user_id,
            @Field(PRODUCT_ID_SERVER) String product_id
    );

    @GET("api/favourite/get")
    Call<ResponseBody> getRecommendedProducts(
            @Query(USER_ID_SERVER) String user_id
    );

    @GET(GET_SCANNED_PRODUCTS_SERVER)
    Call<ResponseBody> getScannedProducts(
            @Query(USER_ID_SERVER) String user_id
    );

    @FormUrlEncoded
    @POST(ADD_SCANNED_PRODUCT_SERVER)
    Call<Void> addScannedProduct(
            @Field(USER_ID_SERVER) String user_id,
            @Field(PRODUCT_BARCODE_SERVER) String product_barcode
    );

    @GET(GET_PRODUCT_REVIEWS_SERVER)
    Call<ResponseBody> getProductReviews(
            @Query(PRODUCT_ID_SERVER) String product_id
    );

    @FormUrlEncoded
    @POST(ADD_PRODUCT_REVIEW_SERVER)
    Call<Void> addProductReview(
            @Field(USER_ID_SERVER) String user_id,
            @Field(PRODUCT_ID_SERVER) String product_id,
            @Field(PRODUCT_REVIEW_RATING_SERVER) float rating,
            @Field(PRODUCT_REVIEW_DESC_SERVER) String description
    );

    @GET(GET_SIMILAR_PRODUCTS_SERVER + "{product_id}")
    Call<ResponseBody> getSimilarProducts(
            @Path("product_id") String product_id,
            @Query(USER_ID_SERVER) String user_id
    );
}
