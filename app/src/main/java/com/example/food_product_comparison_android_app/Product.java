package com.example.food_product_comparison_android_app;

import android.graphics.Bitmap;

public class Product {
    private String barcode;
    private String brand;
    private String name;
    private Float Price;
    private Bitmap product_look_pic;
    private Boolean is_starred;

    public Product(String barcode) {
        this.barcode = barcode;
    }

    public Product(String barcode, String brand, String name, Float price, Bitmap product_look_pic) {
        this.barcode = barcode;
        this.brand = brand;
        this.name = name;
        Price = price;
        this.product_look_pic = product_look_pic;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Float getPrice() {
        return Price;
    }

    public void setPrice(Float price) {
        Price = price;
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
}
