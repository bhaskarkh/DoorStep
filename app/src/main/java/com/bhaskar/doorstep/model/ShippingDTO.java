package com.bhaskar.doorstep.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ShippingDTO  implements Parcelable {
    private String shippingId;
    private UserRegistrationDTO shippingPersonDTO;

    public ShippingDTO(){}
    public ShippingDTO(String shippingId, UserRegistrationDTO shippingPersonDTO) {
        this.shippingId = shippingId;
        this.shippingPersonDTO = shippingPersonDTO;
    }

    protected ShippingDTO(Parcel in) {
        shippingId = in.readString();
        shippingPersonDTO = in.readParcelable(UserRegistrationDTO.class.getClassLoader());
    }

    public static final Creator<ShippingDTO> CREATOR = new Creator<ShippingDTO>() {
        @Override
        public ShippingDTO createFromParcel(Parcel in) {
            return new ShippingDTO(in);
        }

        @Override
        public ShippingDTO[] newArray(int size) {
            return new ShippingDTO[size];
        }
    };

    public String getShippingId() {
        return shippingId;
    }

    public void setShippingId(String shippingId) {
        this.shippingId = shippingId;
    }

    public UserRegistrationDTO getShippingPersonDTO() {
        return shippingPersonDTO;
    }

    public void setShippingPersonDTO(UserRegistrationDTO shippingPersonDTO) {
        this.shippingPersonDTO = shippingPersonDTO;
    }

    @Override
    public String toString() {
        return "ShippingDTO{" +
                "shippingId='" + shippingId + '\'' +
                ", shippingPersonDTO=" + shippingPersonDTO +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shippingId);
        dest.writeParcelable(shippingPersonDTO, flags);
    }
}
