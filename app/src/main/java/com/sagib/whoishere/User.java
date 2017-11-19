package com.sagib.whoishere;

import android.location.Location;

public class User {
    String displayName;
    String profileImg;
    String email;
    String uuid;
    String firebaseUID;
    Location lastKnownLocation;

    public User() {
    }

    public User(String displayName, String profileImg, String email, String uuid, String firebaseUID, Location lastKnownLocation) {

        this.displayName = displayName;
        this.profileImg = profileImg;
        this.email = email;
        this.uuid = uuid;
        this.firebaseUID = firebaseUID;
        this.lastKnownLocation = lastKnownLocation;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFirebaseUID() {
        return firebaseUID;
    }

    public void setFirebaseUID(String firebaseUID) {
        this.firebaseUID = firebaseUID;
    }

    public Location getLastKnownLocation() {
        return lastKnownLocation;
    }

    public void setLastKnownLocation(Location lastKnownLocation) {
        this.lastKnownLocation = lastKnownLocation;
    }

    @Override
    public String toString() {
        return "User{" +
                "displayName='" + displayName + '\'' +
                ", profileImg='" + profileImg + '\'' +
                ", email='" + email + '\'' +
                ", uuid='" + uuid + '\'' +
                ", firebaseUID='" + firebaseUID + '\'' +
                ", lastKnownLocation=" + lastKnownLocation +
                '}';
    }
}
