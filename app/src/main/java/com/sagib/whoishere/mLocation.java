package com.sagib.whoishere;

/**
 * Created by sagib on 21/11/2017.
 */

public class mLocation {
    double lat;
    double lon;
    long time;

    public mLocation(double lat, double lon, long time) {
        this.lat = lat;
        this.lon = lon;
        this.time = time;
    }

    public mLocation() {
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "mLocation{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", time='" + time + '\'' +
                '}';
    }
}
