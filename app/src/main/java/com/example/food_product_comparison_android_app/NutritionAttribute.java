package com.example.food_product_comparison_android_app;

public class NutritionAttribute {
    private String attributeName;
    private String attributeValue;
    private String attributeUnit;

    public NutritionAttribute(String attributeName, String attributeValue, String attributeUnit) {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
        this.attributeUnit = attributeUnit;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public String getAttributeUnit() {
        return attributeUnit;
    }

    public void setAttributeUnit(String attributeUnit) {
        this.attributeUnit = attributeUnit;
    }

    public String getAttributePresentation() {
        return this.getAttributeValue() + " " + this.getAttributeUnit();
    }
}
