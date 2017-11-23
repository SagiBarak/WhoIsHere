package com.sagib.whoishere;

/**
 * Created by sagib on 21/11/2017.
 */

public class FacebookFriend {
    String user;
    String id;
    double distance;

    public FacebookFriend(String user, String id, double distance) {
        this.user = user;
        this.id = id;
        this.distance = distance;
    }

    public FacebookFriend() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "FacebookFriend{" +
                "user='" + user + '\'' +
                ", id='" + id + '\'' +
                ", distance=" + distance +
                '}';
    }
}
