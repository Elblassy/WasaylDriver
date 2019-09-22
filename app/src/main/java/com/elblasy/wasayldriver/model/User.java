package com.elblasy.wasayldriver.model;

public class User {

    private String username, mobile, city;

    public User() {
    }

    public User(String username, String mobile, String city) {
        this.username = username;
        this.mobile = mobile;
        this.city = city;
    }

    //Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


}

