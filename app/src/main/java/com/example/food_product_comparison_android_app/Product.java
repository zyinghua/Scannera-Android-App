package com.example.food_product_comparison_android_app;

public class Product {
    private String barcode;
    private String name;

    public Product(String barcode) {
        this.barcode = barcode;
    }

    public Product(String barcode, String name) {
        this.barcode = barcode;
        this.name = name;
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
