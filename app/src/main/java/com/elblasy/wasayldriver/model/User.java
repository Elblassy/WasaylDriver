package com.elblasy.wasayldriver.model;

public class User {

    private String username, mobile, city, startDate, expiredDate;
    private int rating, ordersCompleted, driverVichel, verified;


    public User() {
    }

    public User(String username, String mobile, String city, String startDate, String expiredDate,
                int rating, int ordersCompleted, int driverVichel, int verified) {
        this.username = username;
        this.mobile = mobile;
        this.city = city;
        this.startDate = startDate;
        this.expiredDate = expiredDate;
        this.rating = rating;
        this.ordersCompleted = ordersCompleted;
        this.driverVichel = driverVichel;
        this.verified = verified;
    }

    public int getVerified() {
        return verified;
    }

    public void setVerified(int verified) {
        this.verified = verified;
    }

    public int getDriverVichel() {
        return driverVichel;
    }

    public void setDriverVichel(int driverVichel) {
        this.driverVichel = driverVichel;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getOrdersCompleted() {
        return ordersCompleted;
    }

    public void setOrdersCompleted(int ordersCompleted) {
        this.ordersCompleted = ordersCompleted;
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

