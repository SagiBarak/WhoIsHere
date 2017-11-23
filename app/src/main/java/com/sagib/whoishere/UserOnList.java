package com.sagib.whoishere;

/**
 * Created by sagib on 22/11/2017.
 */

public class UserOnList {
    User user;
    float distance;

    public UserOnList(User user, float distance) {
        this.user = user;
        this.distance = distance;
    }

    public UserOnList() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "UserOnList{" +
                "user=" + user +
                ", distance=" + distance +
                '}';
    }
}
