package com.bhaskar.doorstep.model;

public class OrderDTO {
    private String orderId;
    private String orderStatus;
    private String orderDateTime;
    private String expectedStartDateOfDelivery;
    private String expectedLastDateOfDelivery;
    private String completeDateTime;
    private String orderConfirmDate;
    private ShippingDTO shippingDTO;
    private UserRegistrationDTO customerInfoDTO;
    private ProductDTO productDTO;
    private boolean isOrderConfirmed;
    private String orderCancelDate;
    private boolean isOrderCanceled;


    public OrderDTO()
    {}

    public OrderDTO(String orderId, String orderStatus, String orderDateTime, String expectedStartDateOfDelivery, String expectedLastDateOfDelivery, String completeDateTime, String orderConfirmDate, ShippingDTO shippingDTO, UserRegistrationDTO customerInfoDTO, ProductDTO productDTO, boolean isOrderConfirmed, String orderCancelDate, boolean isOrderCanceled) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.orderDateTime = orderDateTime;
        this.expectedStartDateOfDelivery = expectedStartDateOfDelivery;
        this.expectedLastDateOfDelivery = expectedLastDateOfDelivery;
        this.completeDateTime = completeDateTime;
        this.orderConfirmDate = orderConfirmDate;
        this.shippingDTO = shippingDTO;
        this.customerInfoDTO = customerInfoDTO;
        this.productDTO = productDTO;
        this.isOrderConfirmed = isOrderConfirmed;
        this.orderCancelDate = orderCancelDate;
        this.isOrderCanceled = isOrderCanceled;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "orderId='" + orderId + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderDateTime='" + orderDateTime + '\'' +
                ", expectedStartDateOfDelivery='" + expectedStartDateOfDelivery + '\'' +
                ", expectedLastDateOfDelivery='" + expectedLastDateOfDelivery + '\'' +
                ", completeDateTime='" + completeDateTime + '\'' +
                ", orderConfirmDate='" + orderConfirmDate + '\'' +
                ", shippingDTO=" + shippingDTO +
                ", customerInfoDTO=" + customerInfoDTO +
                ", productDTO=" + productDTO +
                ", isOrderConfirmed=" + isOrderConfirmed +
                ", orderCancelDate='" + orderCancelDate + '\'' +
                ", isOrderCanceled=" + isOrderCanceled +
                '}';
    }

    public boolean isOrderConfirmed() {
        return isOrderConfirmed;
    }

    public void setOrderConfirmed(boolean orderConfirmed) {
        isOrderConfirmed = orderConfirmed;
    }

    public String getOrderCancelDate() {
        return orderCancelDate;
    }

    public void setOrderCancelDate(String orderCancelDate) {
        this.orderCancelDate = orderCancelDate;
    }

    public boolean isOrderCanceled() {
        return isOrderCanceled;
    }

    public void setOrderCanceled(boolean orderCanceled) {
        isOrderCanceled = orderCanceled;
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

    public String getExpectedStartDateOfDelivery() {
        return expectedStartDateOfDelivery;
    }

    public void setExpectedStartDateOfDelivery(String expectedStartDateOfDelivery) {
        this.expectedStartDateOfDelivery = expectedStartDateOfDelivery;
    }

    public String getExpectedLastDateOfDelivery() {
        return expectedLastDateOfDelivery;
    }

    public void setExpectedLastDateOfDelivery(String expectedLastDateOfDelivery) {
        this.expectedLastDateOfDelivery = expectedLastDateOfDelivery;
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

    public String getOrderConfirmDate() {
        return orderConfirmDate;
    }

    public void setOrderConfirmDate(String orderConfirmDate) {
        this.orderConfirmDate = orderConfirmDate;
    }

}
