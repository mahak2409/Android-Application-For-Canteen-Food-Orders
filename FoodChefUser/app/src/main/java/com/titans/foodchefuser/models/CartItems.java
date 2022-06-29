package com.titans.foodchefuser.models;

public class CartItems {
    private String name;
    private int price;
    private String qty;

    public CartItems(String name, int price, String qty) {
        this.name = name;
        this.price = price;
        this.qty = qty;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
