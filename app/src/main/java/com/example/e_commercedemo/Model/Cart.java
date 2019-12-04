package com.example.e_commercedemo.Model;

public class Cart
{
    private String pId,pName,price,description,quantity,discount;


    public Cart() {
    }

    public Cart(String pId, String pName, String price, String description, String quantity, String discount) {
        this.pId = pId;
        this.pName = pName;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.discount = discount;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}


