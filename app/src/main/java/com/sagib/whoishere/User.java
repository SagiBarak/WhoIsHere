package com.sagib.whoishere;

public class User {
    String displayName;
    String profileImg;
    String email;
    String uuid;
    String firebaseUID;
    String facebookUID;
    mLocation mLocation;

    public User(String displayName, String profileImg, String email, String uuid, String firebaseUID, String facebookUID, com.sagib.whoishere.mLocation mLocation) {
        this.displayName = displayName;
        this.profileImg = profileImg;
        this.email = email;
        this.uuid = uuid;
        this.firebaseUID = firebaseUID;
        this.facebookUID = facebookUID;
        this.mLocation = mLocation;
    }

    public User() {
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

    public com.sagib.whoishere.mLocation getmLocation() {
        return mLocation;
    }

    public void setmLocation(com.sagib.whoishere.mLocation mLocation) {
        this.mLocation = mLocation;
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
                ", mLocation=" + mLocation +
                '}';
    }
}
