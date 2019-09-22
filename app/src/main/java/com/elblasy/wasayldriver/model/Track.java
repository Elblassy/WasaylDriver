package com.elblasy.wasayldriver.model;

public class Track {
    boolean wayToTarget, wayToHome,delivered;

    public Track() {
    }

    public Track(boolean wayToTarget, boolean wayToHome, boolean delivered) {
        this.wayToTarget = wayToTarget;
        this.wayToHome = wayToHome;
        this.delivered = delivered;

    }

    public boolean isWayToTarget() {
        return wayToTarget;
    }

    public void setWayToTarget(boolean wayToTarget) {
        this.wayToTarget = wayToTarget;
    }

    public boolean isWayToHome() {
        return wayToHome;
    }

    public void setWayToHome(boolean wayToHome) {
        this.wayToHome = wayToHome;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }
}
