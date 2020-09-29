package com.bhaskar.doorstep.model;

public class ShippingDTO {
    private String shippingId;
    private UserRegistrationDTO shippingPersonDTO;

    public ShippingDTO(){}
    public ShippingDTO(String shippingId, UserRegistrationDTO shippingPersonDTO) {
        this.shippingId = shippingId;
        this.shippingPersonDTO = shippingPersonDTO;
    }

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
}
