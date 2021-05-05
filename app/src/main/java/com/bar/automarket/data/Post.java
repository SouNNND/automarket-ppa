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

    public Post() {}

    public Post(String make, String model, String fuel, String displacement, String power, String mileage, String year, String userId) {
        this.make = make;
        this.model = model;
        this.fuel = fuel;
        this.displacement = displacement;
        this.power = power;
        this.mileage = mileage;
        this.year = year;
        this.userId = userId;
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
}
