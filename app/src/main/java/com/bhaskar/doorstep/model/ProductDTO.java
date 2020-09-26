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
    private String supplierName;
    private String supplierId;


    public ProductDTO()
    {}

    public ProductDTO(String id, String name, String category, String productTypeId, String description, String image, String quantityType, boolean isDelevieryPersonRequired, String supplierName, String supplierId) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.productTypeId = productTypeId;
        Description = description;
        this.image = image;
        this.quantityType = quantityType;
        this.isDelevieryPersonRequired = isDelevieryPersonRequired;
        this.supplierName = supplierName;
        this.supplierId = supplierId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(String productTypeId) {
        this.productTypeId = productTypeId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(String quantityType) {
        this.quantityType = quantityType;
    }

    public boolean isDelevieryPersonRequired() {
        return isDelevieryPersonRequired;
    }

    public void setDelevieryPersonRequired(boolean delevieryPersonRequired) {
        isDelevieryPersonRequired = delevieryPersonRequired;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", productTypeId='" + productTypeId + '\'' +
                ", Description='" + Description + '\'' +
                ", image='" + image + '\'' +
                ", quantityType='" + quantityType + '\'' +
                ", isDelevieryPersonRequired=" + isDelevieryPersonRequired +
                ", supplierName='" + supplierName + '\'' +
                ", supplierId='" + supplierId + '\'' +
                '}';
    }
}
