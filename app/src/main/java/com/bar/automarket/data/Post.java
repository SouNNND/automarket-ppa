package com.bar.automarket.data;

public class Post {

    private String make;
    private String model;
    private String fuel;
    private String displacement;
    private String power;
    private String mileage;
    private String year;
    private String userId;
    private String imgId;
    private String price;

    public Post() { }

    public Post(String make, String model, String fuel, String displacement, String power, String mileage, String year, String userId, String imgId, String price) {
        this.make = make;
        this.model = model;
        this.fuel = fuel;
        this.displacement = displacement;
        this.power = power;
        this.mileage = mileage;
        this.year = year;
        this.userId = userId;
        this.imgId = imgId;
        this.price = price;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getFuel() {
        return fuel;
    }

    public String getDisplacement() {
        return displacement;
    }

    public String getPower() {
        return power;
    }

    public String getMileage() {
        return mileage;
    }

    public String getYear() {
        return year;
    }

    public String getUserId() {
        return userId;
    }

    public String getImgId() { return imgId; }

    public String getPrice() { return price; }
}
