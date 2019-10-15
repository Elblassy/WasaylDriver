package com.elblasy.wasayldriver.model;

public class Orders {

    private boolean active;
    private String details, placeName, userName, from, token, phoneNumber, to,
            driversID, status, pushID, kindOfOrder;

    public Orders() {
    }

    public Orders(boolean active, String details, String from, String placeName,
                      String userName, String token, String phoneNumber, String to, String driversID,
                      String status, String pushID, String kindOfOrder) {
        this.phoneNumber = phoneNumber;
        this.active = active;
        this.details = details;
        this.from = from;
        this.token = token;
        this.placeName = placeName;
        this.userName = userName;
        this.to = to;
        this.driversID = driversID;
        this.status = status;
        this.pushID = pushID;
        this.kindOfOrder = kindOfOrder;
    }

    public String getKindOfOrder() {
        return kindOfOrder;
    }

    public void setKindOfOrder(String kindOfOrder) {
        this.kindOfOrder = kindOfOrder;
    }

    public String getPushID() {
        return pushID;
    }

    public void setPushID(String pushID) {
        this.pushID = pushID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
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
