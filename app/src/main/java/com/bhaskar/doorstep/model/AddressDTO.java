package com.bhaskar.doorstep.model;

public class AddressDTO {
    private String name;
    private String address;
    private String city;
    private String state;
    private int pincode;
    private String delevieryMobile;
    private String typeOfAddress;

    AddressDTO()
    {}

    public AddressDTO(String name, String address, String city, String state, int pincode, String delevieryMobile, String typeOfAddress) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.delevieryMobile = delevieryMobile;
        this.typeOfAddress = typeOfAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
        return delevieryMobile;
    }

    public void setDelevieryMobile(String delevieryMobile) {
        this.delevieryMobile = delevieryMobile;
    }

    public String getTypeOfAddress() {
        return typeOfAddress;
    }

    public void setTypeOfAddress(String typeOfAddress) {
        this.typeOfAddress = typeOfAddress;
    }

    @Override
    public String toString() {
        return "AddressDTO{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", pincode=" + pincode +
                ", delevieryMobile='" + delevieryMobile + '\'' +
                ", typeOfAddress='" + typeOfAddress + '\'' +
                '}';
    }
}
