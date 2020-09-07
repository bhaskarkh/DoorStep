package com.bhaskar.doorstep.model;

public class ProductDTO {
    private String id; //unique id for all product
    private String name;
    private String category;
    private String productTypeId; //like for banana there will be fixed id
    private String Description;
    private String image;
    private String quantityType; //kg litre dozen number
    private boolean isDelevieryPersonRequired; //like for tailor shopkeeper itself is a deleviery person
}
