package com.example.vendor.models;

import android.graphics.Bitmap;

public class FoodItems {

    String foodName="";
    String price="";

    String calories="";
    String ingredients="";

    String vegOrNonVeg="";
    String bestsellerOrNot="";

    String foodImgUrl;


    public FoodItems(String foodName, String price, String calories , String ingredients , String vegOrNonVeg , String bestsellerOrNot ,String foodImgUrl )
    {
        this.foodName=foodName;
        this.price=price;
        this.calories=calories;
        this.ingredients=ingredients;
        this.vegOrNonVeg=vegOrNonVeg;
        this.bestsellerOrNot=bestsellerOrNot;
        this.foodImgUrl=foodImgUrl;
    }


    // creating getter and setter methods.
    public String getFoodName() {
        return foodName;
    }
    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }


    public String getCalories() {     return calories;   }
    public void setCalories(String calories) {
        this.calories = calories;
    }


    public String getIngredients() {
        return ingredients;
    }
    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }


    public String getVegOrNonVeg() {
        return vegOrNonVeg;
    }
    public void setVegOrNonVeg(String vegOrNonVeg) {  this.vegOrNonVeg = vegOrNonVeg;    }


    public String getBestsellerOrNot() {
        return bestsellerOrNot;
    }
    public void setBestsellerOrNot(String bestsellerOrNot) {  this.bestsellerOrNot = bestsellerOrNot;  }

    public String getFoodImgUrl() {return foodImgUrl;}
    public  void setFoodImgUrl(String foodImgUrl){this.foodImgUrl=foodImgUrl; }


}
