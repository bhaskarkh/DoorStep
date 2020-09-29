package com.bhaskar.doorstep.model;

import android.net.Uri;

import java.util.Date;
import java.util.List;

public class UserRegistrationDTO {
    private String userName;
    private String userEmail;
    private  String userId;
    private String userPhoto;
    private String role;
    private String rmn;
    private boolean isRmnVerified;
    private String  userRegisteredDate;
    private String userStatus;
    private AddressDTO addressDTO;


    public UserRegistrationDTO() {
    }

    public UserRegistrationDTO(String userName, String userEmail, String userId, String userPhoto, String role, String rmn, boolean isRmnVerified, String userRegisteredDate, String userStatus, AddressDTO addressList) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userId = userId;
        this.userPhoto = userPhoto;
        this.role = role;
        this.rmn = rmn;
        this.isRmnVerified = isRmnVerified;
        this.userRegisteredDate = userRegisteredDate;
        this.userStatus = userStatus;
        this.addressDTO = addressList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRmn() {
        return rmn;
    }

    public void setRmn(String rmn) {
        this.rmn = rmn;
    }

    public boolean isRmnVerified() {
        return isRmnVerified;
    }

    public void setRmnVerified(boolean rmnVerified) {
        isRmnVerified = rmnVerified;
    }

    public String getUserRegisteredDate() {
        return userRegisteredDate;
    }

    public void setUserRegisteredDate(String userRegisteredDate) {
        this.userRegisteredDate = userRegisteredDate;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public AddressDTO getAddressList() {
        return addressDTO;
    }

    public void setAddressList(AddressDTO addressList) {
        this.addressDTO = addressList;
    }

    @Override
    public String toString() {
        return "UserRegistrationDTO{" +
                "userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userId='" + userId + '\'' +
                ", userPhoto='" + userPhoto + '\'' +
                ", role='" + role + '\'' +
                ", rmn='" + rmn + '\'' +
                ", isRmnVerified=" + isRmnVerified +
                ", userRegisteredDate='" + userRegisteredDate + '\'' +
                ", userStatus='" + userStatus + '\'' +
                ", addressDTO=" + addressDTO +
                '}';
    }
}
