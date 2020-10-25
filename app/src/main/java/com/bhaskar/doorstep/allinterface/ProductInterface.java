package com.bhaskar.doorstep.allinterface;

import com.bhaskar.doorstep.model.ProductDTO;

import java.util.List;

public interface ProductInterface {
    public  void onProductAddedinDb();
    public void onProductAddFailed();
    public void setProductListToRecyclerView(List<ProductDTO> productDTOList);

}
