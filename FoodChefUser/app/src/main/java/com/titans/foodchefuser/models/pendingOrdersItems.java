package com.titans.foodchefuser.models;

public class pendingOrdersItems {

    String name="";
    String orderList="";
    String phone="";
    String totalBillAmount="";

    public pendingOrdersItems(){} //Empty constructor is needed for firestore database

    public pendingOrdersItems(String name, String orderList, String phone, String totalBillAmount) {
        this.name = name;
        this.orderList = orderList;
        this.phone = phone;
        this.totalBillAmount = totalBillAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderList() {
        return orderList;
    }

    public void setOrderList(String orderList) {
        this.orderList = orderList;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTotalBillAmount() {
        return totalBillAmount;
    }

    public void setTotalBillAmount(String totalBillAmount) {
        this.totalBillAmount = totalBillAmount;
    }
}
