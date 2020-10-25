package com.bhaskar.doorstep.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductDTO implements Parcelable {
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
    private int amountAvailable;
    private double myPrice;
    private double mrpPrice;
    private boolean showCost;
    private int quantity;
    private boolean isDiscountProduct;


    public ProductDTO()
    {}

    public ProductDTO(String id, String name, String category, String productTypeId, String description, String image, String quantityType, boolean isDelevieryPersonRequired, String supplierName, String supplierId, int amountAvailable, double myPrice, double mrpPrice, boolean showCost, int quantity,boolean isDiscountProduct) {
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
        this.amountAvailable = amountAvailable;
        this.myPrice = myPrice;
        this.mrpPrice = mrpPrice;
        this.showCost = showCost;
        this.quantity = quantity;
        this.isDiscountProduct=isDiscountProduct;
    }


    protected ProductDTO(Parcel in) {
        id = in.readString();
        name = in.readString();
        category = in.readString();
        productTypeId = in.readString();
        Description = in.readString();
        image = in.readString();
        quantityType = in.readString();
        isDelevieryPersonRequired = in.readByte() != 0;
        supplierName = in.readString();
        supplierId = in.readString();
        amountAvailable = in.readInt();
        myPrice = in.readDouble();
        mrpPrice = in.readDouble();
        showCost = in.readByte() != 0;
        quantity = in.readInt();
        isDiscountProduct=in.readByte() != 0;

    }

    public static final Creator<ProductDTO> CREATOR = new Creator<ProductDTO>() {
        @Override
        public ProductDTO createFromParcel(Parcel in) {
            return new ProductDTO(in);
        }

        @Override
        public ProductDTO[] newArray(int size) {
            return new ProductDTO[size];
        }
    };

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

    public int getAmountAvailable() {
        return amountAvailable;
    }

    public void setAmountAvailable(int amountAvailable) {
        this.amountAvailable = amountAvailable;
    }

    public double getPrice() {
        return myPrice;
    }

    public void setPrice(double price) {
        this.myPrice = price;
    }

    public boolean isShowCost() {
        return showCost;
    }

    public void setShowCost(boolean showCost) {
        this.showCost = showCost;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getMyPrice() {
        return myPrice;
    }

    public void setMyPrice(double myPrice) {
        this.myPrice = myPrice;
    }

    public double getMrpPrice() {
        return mrpPrice;
    }

    public void setMrpPrice(double mrpPrice) {
        this.mrpPrice = mrpPrice;
    }

    public boolean isDiscountProduct() {
        return isDiscountProduct;
    }

    public void setDiscountProduct(boolean discountProduct) {
        isDiscountProduct = discountProduct;
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
                ", amountAvailable=" + amountAvailable +
                ", myPrice=" + myPrice +
                ", mrpPrice=" + mrpPrice +
                ", showCost=" + showCost +
                ", quantity=" + quantity +
                ", isDiscountProduct=" + isDiscountProduct +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if(o!=null && o instanceof ProductDTO)
        {
            String cat=((ProductDTO)o).getId();
            if (cat!=null && cat.equals(this.id))
            {
                return true;
            }

        }

        return false;
    }
    @Override
    public int hashCode()
    {
        return this.category.hashCode();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(category);
        dest.writeString(productTypeId);
        dest.writeString(Description);
        dest.writeString(image);
        dest.writeString(quantityType);
        dest.writeByte((byte) (isDelevieryPersonRequired ? 1 : 0));
        dest.writeString(supplierName);
        dest.writeString(supplierId);
        dest.writeInt(amountAvailable);
        dest.writeDouble(myPrice);
        dest.writeDouble(mrpPrice);
        dest.writeByte((byte) (showCost ? 1 : 0));
        dest.writeInt(quantity);
        dest.writeByte((byte) (isDiscountProduct ? 1 : 0));
    }
}
