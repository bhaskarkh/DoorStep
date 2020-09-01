package com.bhaskar.doorstep.model;

import android.net.Uri;


public class GoogleSignInDTO {
    String userName;
    String userGivenName;
    String userFamilyName;
    String userEmail;
    String userId;
    Uri userPhoto;


    public GoogleSignInDTO(){}
    public GoogleSignInDTO(String userName, String userGivenName, String userFamilyName, String userEmail, String userId, Uri userPhoto) {
        this.userName = userName;
        this.userGivenName = userGivenName;
        this.userFamilyName = userFamilyName;
        this.userEmail = userEmail;
        this.userId = userId;
        this.userPhoto = userPhoto;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserGivenName() {
        return userGivenName;
    }

    public void setUserGivenName(String userGivenName) {
        this.userGivenName = userGivenName;
    }

    public String getUserFamilyName() {
        return userFamilyName;
    }

    public void setUserFamilyName(String userFamilyName) {
        this.userFamilyName = userFamilyName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Uri getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(Uri userPhoto) {
        this.userPhoto = userPhoto;
    }

    @Override
    public String toString() {
        return "GoogleSignInDTO{" +
                "userName='" + userName + '\'' +
                ", userGivenName='" + userGivenName + '\'' +
                ", userFamilyName='" + userFamilyName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userId='" + userId + '\'' +
                ", userPhoto=" + userPhoto +
                '}';
    }
}
