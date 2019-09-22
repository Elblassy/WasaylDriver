package com.elblasy.wasayldriver.model;

public class Orders {

    private boolean active;
    private String details, placeName, userName, form, token, phoneNumber, to,driversID;

    public Orders() {
    }

    public Orders(boolean active, String details, String form, String placeName,
                      String userName, String token, String phoneNumber, String to,String driversID) {
        this.phoneNumber = phoneNumber;
        this.active = active;
        this.details = details;
        this.form = form;
        this.token = token;
        this.placeName = placeName;
        this.userName = userName;
        this.to = to;
        this.driversID = driversID;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getDriversID() {
        return driversID;
    }

    public void setDriversID(String driversID) {
        this.driversID = driversID;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
