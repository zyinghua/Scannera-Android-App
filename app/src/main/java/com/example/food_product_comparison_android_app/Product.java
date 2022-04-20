package com.example.food_product_comparison_android_app;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

public class Product {
    @SerializedName(ServerRetrofitAPI.PRODUCT_ID_SERVER)
    private String productId;

    @SerializedName(ServerRetrofitAPI.PRODUCT_BARCODE_SERVER)
    private String barcode;

    @SerializedName(ServerRetrofitAPI.PRODUCT_BRAND_SERVER)
    private String brand;

    @SerializedName(ServerRetrofitAPI.PRODUCT_NAME_SERVER)
    private String name;

    @SerializedName(ServerRetrofitAPI.PRODUCT_PRICE_SERVER)
    private Double price;

    @SerializedName(ServerRetrofitAPI.PRODUCT_CATEGORY_SERVER)
    private String category;

    @SerializedName(ServerRetrofitAPI.PRODUCT_IS_STARRED_SERVER)
    private Boolean isStarred;

    private HashMap<String, NutritionAttribute> nutritionAttributes;

    private String productImgUrl;

    public Product() {
    }

    public Product(String product_id, String barcode, String brand, String name, Double price, String category, Boolean isStarred) {
        this.productId = product_id;
        this.barcode = barcode;
        this.brand = brand;
        this.name = name;
        this.price = price;
        this.category = category;
        this.isStarred = isStarred;
        this.nutritionAttributes = new HashMap<>();
    }

    public Product(String product_id, String barcode, String brand, String name, Double price, String category, Boolean isStarred, String productImgUrl) {
        this.productId = product_id;
        this.barcode = barcode;
        this.brand = brand;
        this.name = name;
        this.price = price;
        this.category = category;
        this.isStarred = isStarred;
        this.nutritionAttributes = new HashMap<>();
        this.productImgUrl = productImgUrl;
    }


    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Double getPrice() {
        return price;
    }

    public String getPriceInString()
    {
        return "$" + price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getProductImgUrl() {
        return productImgUrl;
    }

    public void setProductImgUrl(String productImgUrl) {
        this.productImgUrl = productImgUrl;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Boolean getStarred() {
        return isStarred;
    }

    public void setStarred(Boolean starred) {
        isStarred = starred;
    }

    public HashMap<String, NutritionAttribute> getNutritionAttributes() {
        return nutritionAttributes;
    }

    public void setNutritionAttributes(HashMap<String, NutritionAttribute> nutritionAttributes) {
        this.nutritionAttributes = nutritionAttributes;
    }

    public float getSpecificNutritionValue(String factor)
    {
        return this.nutritionAttributes.get(factor).getAttributeValue();
    }
}
