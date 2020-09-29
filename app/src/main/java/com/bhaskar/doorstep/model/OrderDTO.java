package com.bhaskar.doorstep.model;

public class OrderDTO {
    private String orderId;
    private String orderStatus;
    private String orderDateTime;
    private String expectedStartDateOfDeleviery;
    private String expectedLastDateOfDeleviery;
    private String completeDateTime;
    private ShippingDTO shippingDTO;
    private UserRegistrationDTO customerInfoDTO;
    private ProductDTO productDTO;

    public OrderDTO()
    {}

    public OrderDTO(String orderId, String orderStatus, String orderDateTime, String expectedStartDateOfDeleviery, String expectedLastDateOfDeleviery, String completeDateTime, ShippingDTO shippingDTO, UserRegistrationDTO customerInfoDTO, ProductDTO productDTO) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.orderDateTime = orderDateTime;
        this.expectedStartDateOfDeleviery = expectedStartDateOfDeleviery;
        this.expectedLastDateOfDeleviery = expectedLastDateOfDeleviery;
        this.completeDateTime = completeDateTime;
        this.shippingDTO = shippingDTO;
        this.customerInfoDTO = customerInfoDTO;
        this.productDTO = productDTO;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(String orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public String getExpectedStartDateOfDeleviery() {
        return expectedStartDateOfDeleviery;
    }

    public void setExpectedStartDateOfDeleviery(String expectedStartDateOfDeleviery) {
        this.expectedStartDateOfDeleviery = expectedStartDateOfDeleviery;
    }

    public String getExpectedLastDateOfDeleviery() {
        return expectedLastDateOfDeleviery;
    }

    public void setExpectedLastDateOfDeleviery(String expectedLastDateOfDeleviery) {
        this.expectedLastDateOfDeleviery = expectedLastDateOfDeleviery;
    }

    public String getCompleteDateTime() {
        return completeDateTime;
    }

    public void setCompleteDateTime(String completeDateTime) {
        this.completeDateTime = completeDateTime;
    }

    public ShippingDTO getShippingDTO() {
        return shippingDTO;
    }

    public void setShippingDTO(ShippingDTO shippingDTO) {
        this.shippingDTO = shippingDTO;
    }

    public UserRegistrationDTO getCustomerInfoDTO() {
        return customerInfoDTO;
    }

    public void setCustomerInfoDTO(UserRegistrationDTO customerInfoDTO) {
        this.customerInfoDTO = customerInfoDTO;
    }

    public ProductDTO getProductDTO() {
        return productDTO;
    }

    public void setProductDTO(ProductDTO productDTO) {
        this.productDTO = productDTO;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "orderId='" + orderId + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderDateTime='" + orderDateTime + '\'' +
                ", expectedStartDateOfDeleviery='" + expectedStartDateOfDeleviery + '\'' +
                ", expectedLastDateOfDeleviery='" + expectedLastDateOfDeleviery + '\'' +
                ", completeDateTime='" + completeDateTime + '\'' +
                ", shippingDTO=" + shippingDTO +
                ", customerInfoDTO=" + customerInfoDTO +
                ", productDTO=" + productDTO +
                '}';
    }
}
