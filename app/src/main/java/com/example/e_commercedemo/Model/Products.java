package com.example.e_commercedemo.Model;

public class Products {
    private String pName,description,price,imageUrl,category,pId,date,time;


    public Products() {
    }

    public Products(String pName, String description, String price, String imageUrl, String category, String pId, String date, String time) {
        this.pName = pName;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.pId = pId;
        this.date = date;
        this.time = time;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
