package com.example.food_product_comparison_android_app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Product {
    @SerializedName(ServerAPI.PRODUCT_ID_SERVER)
    private String productId;

    @SerializedName(ServerAPI.PRODUCT_BARCODE_SERVER)
    private String barcode;

    @SerializedName(ServerAPI.PRODUCT_BRAND_SERVER)
    private String brand;

    @SerializedName(ServerAPI.PRODUCT_NAME_SERVER)
    private String name;

    @SerializedName(ServerAPI.PRODUCT_PRICE_SERVER)
    private Double price;

    @SerializedName(ServerAPI.PRODUCT_CATEGORY_SERVER)
    private String category;

    private Bitmap product_look_pic;

    @SerializedName(ServerAPI.PRODUCT_IS_STARRED_SERVER)
    private Boolean isStarred;

    private ArrayList<NutritionAttribute> nutritionAttributes;

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
    }

    public Product(String product_id, String barcode, String brand, String name, Double price, String category, Boolean isStarred, ArrayList<NutritionAttribute> nutritionAttributes) {
        this.productId = product_id;
        this.barcode = barcode;
        this.brand = brand;
        this.name = name;
        this.price = price;
        this.category = category;
        this.isStarred = isStarred;
        this.nutritionAttributes = nutritionAttributes;
    }

    public Product(String barcode, String brand, String name, Double price, String category, Bitmap product_look_pic, Boolean isStarred) {
        this.productId = productId;
        this.barcode = barcode;
        this.brand = brand;
        this.name = name;
        this.price = price;
        this.category = category;
        this.product_look_pic = product_look_pic;
        this.isStarred = isStarred;
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

    public void setPrice(Double price) {
        this.price = price;
    }

    public Bitmap getProduct_look_pic() {
        return product_look_pic;
    }

    public void setProduct_look_pic(Bitmap product_look_pic) {
        this.product_look_pic = product_look_pic;
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

    public ArrayList<NutritionAttribute> getNutritionAttributes() {
        return nutritionAttributes;
    }

    public void setNutritionAttributes(ArrayList<NutritionAttribute> nutritionAttributes) {
        this.nutritionAttributes = nutritionAttributes;
    }

}
