package com.bhaskar.doorstep.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AddressDTO implements Parcelable {
    private int id;
    private String name;
    private String house_no;
    private String area_colony;
    private String landmark;
    private String city;
    private String state;
    private int pincode;
    private String deliveryMobileNo;
    private String typeOfAddress;
    private boolean isPrimaryAddress;

   public AddressDTO()
    {}
    public AddressDTO(AddressDTO addressDTO)
    {
        this.id = addressDTO.getId();
        this.name = addressDTO.getName();
        this.house_no = addressDTO.getHouse_no();
        this.area_colony = addressDTO.getArea_colony();
        this.landmark = "";
        this.city = addressDTO.getCity();
        this.state = addressDTO.getState();
        this.pincode = addressDTO.getPincode();
        this.deliveryMobileNo = addressDTO.getDeliveryMobileNo();
        this.typeOfAddress = "home";
        this.isPrimaryAddress = addressDTO.isPrimaryAddress();
    }

    public AddressDTO(int id, String name, String house_no, String area_colony, String landmark, String city, String state, int pincode, String deliveryMobileNo, String typeOfAddress, boolean isPrimaryAddress) {
        this.id = id;
        this.name = name;
        this.house_no = house_no;
        this.area_colony = area_colony;
        this.landmark = landmark;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.deliveryMobileNo = deliveryMobileNo;
        this.typeOfAddress = typeOfAddress;
        this.isPrimaryAddress = isPrimaryAddress;
    }


    protected AddressDTO(Parcel in) {
        id = in.readInt();
        name = in.readString();
        house_no = in.readString();
        area_colony = in.readString();
        landmark = in.readString();
        city = in.readString();
        state = in.readString();
        pincode = in.readInt();
        deliveryMobileNo = in.readString();
        typeOfAddress = in.readString();
        isPrimaryAddress = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(house_no);
        dest.writeString(area_colony);
        dest.writeString(landmark);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeInt(pincode);
        dest.writeString(deliveryMobileNo);
        dest.writeString(typeOfAddress);
        dest.writeByte((byte) (isPrimaryAddress ? 1 : 0));
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AddressDTO> CREATOR = new Creator<AddressDTO>() {
        @Override
        public AddressDTO createFromParcel(Parcel in) {
            return new AddressDTO(in);
        }

        @Override
        public AddressDTO[] newArray(int size) {
            return new AddressDTO[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHouse_no() {
        return house_no;
    }

    public void setHouse_no(String house_no) {
        this.house_no = house_no;
    }

    public String getArea_colony() {
        return area_colony;
    }

    public void setArea_colony(String area_colony) {
        this.area_colony = area_colony;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

    public String getDeliveryMobileNo() {
        return deliveryMobileNo;
    }

    public void setDeliveryMobileNo(String deliveryMobileNo) {
        this.deliveryMobileNo = deliveryMobileNo;
    }

    public String getTypeOfAddress() {
        return typeOfAddress;
    }

    public void setTypeOfAddress(String typeOfAddress) {
        this.typeOfAddress = typeOfAddress;
    }

    public boolean isPrimaryAddress() {
        return isPrimaryAddress;
    }

    public void setPrimaryAddress(boolean primaryAddress) {
        isPrimaryAddress = primaryAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AddressDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", house_no='" + house_no + '\'' +
                ", area_colony='" + area_colony + '\'' +
                ", landmark='" + landmark + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", pincode=" + pincode +
                ", deliveryMobileNo='" + deliveryMobileNo + '\'' +
                ", typeOfAddress='" + typeOfAddress + '\'' +
                ", isPrimaryAddress=" + isPrimaryAddress +
                '}';
    }
}
