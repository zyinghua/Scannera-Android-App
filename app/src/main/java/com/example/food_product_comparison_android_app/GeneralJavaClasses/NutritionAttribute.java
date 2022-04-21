package com.example.food_product_comparison_android_app.GeneralJavaClasses;

public class NutritionAttribute {
    private String attributeName;
    private float attributeValue;
    private String attributeUnit;

    public NutritionAttribute(String attributeName, float attributeValue, String attributeUnit) {
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

    public float getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(float attributeValue) {
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
