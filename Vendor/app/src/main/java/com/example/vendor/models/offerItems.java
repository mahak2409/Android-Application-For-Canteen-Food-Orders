package com.example.vendor.models;

public class offerItems {


    String couponCode="";
    String discountPercentage="";


    public offerItems(String couponName, String couponCode, String discountPercentage )
    {
        this.couponCode=couponCode;
        this.discountPercentage=discountPercentage;
    }


    public String getCouponCode() {
        return couponCode;
    }
    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }


    public String getDiscountPercentage() {     return discountPercentage;   }
    public void setDiscountPercentage(String discountPercentage)
    {
        this.discountPercentage = discountPercentage;
    }



}
