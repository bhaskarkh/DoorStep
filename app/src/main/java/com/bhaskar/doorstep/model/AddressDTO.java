package com.bhaskar.doorstep.model;

public class AddressDTO {
    private String name;
    private String house_no;
    private String area_colony;
    private String landmark;
    private String city;
    private String state;
    private int pincode;
    private String deliveryMobileNo;
    private String typeOfAddress;

    AddressDTO()
    {}

    public AddressDTO(String name, String house_no, String area_colony, String landmark, String city, String state, int pincode, String deliveryMobileNo, String typeOfAddress) {
        this.name = name;
        this.house_no = house_no;
        this.area_colony = area_colony;
        this.landmark = landmark;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.deliveryMobileNo = deliveryMobileNo;
        this.typeOfAddress = typeOfAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDelevieryMobile() {
        return deliveryMobileNo;
    }

    public void setDelevieryMobile(String delevieryMobile) {
        this.deliveryMobileNo = delevieryMobile;
    }

    public String getTypeOfAddress() {
        return typeOfAddress;
    }

    public void setTypeOfAddress(String typeOfAddress) {
        this.typeOfAddress = typeOfAddress;
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

    @Override
    public String toString() {
        return "AddressDTO{" +
                "name='" + name + '\'' +
                ", house_no='" + house_no + '\'' +
                ", area_colony='" + area_colony + '\'' +
                ", landmark='" + landmark + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", pincode=" + pincode +
                ", delevieryMobile='" + deliveryMobileNo + '\'' +
                ", typeOfAddress='" + typeOfAddress + '\'' +
                '}';
    }
}
