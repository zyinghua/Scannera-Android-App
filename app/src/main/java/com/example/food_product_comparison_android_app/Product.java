package com.example.food_product_comparison_android_app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Product {
    private String barcode;
    private String brand;
    private String name;
    private Float price;
    private String category;
    private Bitmap product_look_pic;
    private Boolean is_starred;

    public Boolean getIs_starred() {
        return is_starred;
    }

    public void setIs_starred(Boolean is_starred) {
        this.is_starred = is_starred;
    }

    public Product(String barcode) {
        this.barcode = barcode;
    }

    public Product(String barcode, String brand, String name, Float price, String category, Boolean is_starred) {
        this.barcode = barcode;
        this.brand = brand;
        this.name = name;
        this.price = price;
        this.category = category;
        this.is_starred = is_starred;
    }

    public Product(String barcode, String brand, String name, Float price, String category, Bitmap product_look_pic, Boolean is_starred) {
        this.barcode = barcode;
        this.brand = brand;
        this.name = name;
        this.price = price;
        this.category = category;
        this.product_look_pic = product_look_pic;
        this.is_starred = is_starred;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        price = price;
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
}
