package com.bar.automarket.data;

public class User {
    private String email;
    private String phone;
    private String username;

    public User() {}

    public User(String email, String phone, String username) {
        this.email = email;
        this.phone = phone;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getUsername() {
        return username;
    }
}
