package com.sagib.whoishere;

import android.location.Location;

public class User {
    String displayName;
    String profileImg;
    String email;
    String uuid;
    String firebaseUID;
    String facebookUID;
    Location lastKnownLocation;

    public User() {
    }

    public User(String displayName, String profileImg, String email, String uuid, String firebaseUID, String facebookUID, Location lastKnownLocation) {
        this.displayName = displayName;
        this.profileImg = profileImg;
        this.email = email;
        this.uuid = uuid;
        this.firebaseUID = firebaseUID;
        this.facebookUID = facebookUID;
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

    public String getFacebookUID() {
        return facebookUID;
    }

    public void setFacebookUID(String facebookUID) {
        this.facebookUID = facebookUID;
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
                ", facebookUID='" + facebookUID + '\'' +
                ", lastKnownLocation=" + lastKnownLocation +
                '}';
    }
}
