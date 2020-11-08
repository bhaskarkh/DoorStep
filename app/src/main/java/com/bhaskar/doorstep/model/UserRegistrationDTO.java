package com.bhaskar.doorstep.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

public class UserRegistrationDTO implements Parcelable {
    private String userName;
    private String userEmail;
    private  String userId;
    private String userPhoto;
    private String role;
    private String rmn;
    private boolean isRmnVerified;
    private String  userRegisteredDate;
    private String userStatus;
    private List<AddressDTO> addressDTOList;


    public UserRegistrationDTO() {
    }

    public UserRegistrationDTO(String userName, String userEmail, String userId, String userPhoto, String role, String rmn, boolean isRmnVerified, String userRegisteredDate, String userStatus, List<AddressDTO> addressDTOList) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userId = userId;
        this.userPhoto = userPhoto;
        this.role = role;
        this.rmn = rmn;
        this.isRmnVerified = isRmnVerified;
        this.userRegisteredDate = userRegisteredDate;
        this.userStatus = userStatus;
        this.addressDTOList = addressDTOList;
    }

    protected UserRegistrationDTO(Parcel in) {
        userName = in.readString();
        userEmail = in.readString();
        userId = in.readString();
        userPhoto = in.readString();
        role = in.readString();
        rmn = in.readString();
        isRmnVerified = in.readByte() != 0;
        userRegisteredDate = in.readString();
        userStatus = in.readString();
        addressDTOList = in.createTypedArrayList(AddressDTO.CREATOR);
    }

    public static final Creator<UserRegistrationDTO> CREATOR = new Creator<UserRegistrationDTO>() {
        @Override
        public UserRegistrationDTO createFromParcel(Parcel in) {
            return new UserRegistrationDTO(in);
        }

        @Override
        public UserRegistrationDTO[] newArray(int size) {
            return new UserRegistrationDTO[size];
        }
    };

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

    public List<AddressDTO> getAddressDTOList() {
        return addressDTOList;
    }

    public void setAddressDTOList(List<AddressDTO> addressDTOList) {
        this.addressDTOList = addressDTOList;
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
                ", addressDTOList=" + addressDTOList +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(userEmail);
        dest.writeString(userId);
        dest.writeString(userPhoto);
        dest.writeString(role);
        dest.writeString(rmn);
        dest.writeByte((byte) (isRmnVerified ? 1 : 0));
        dest.writeString(userRegisteredDate);
        dest.writeString(userStatus);
        dest.writeTypedList(addressDTOList);
    }
}
