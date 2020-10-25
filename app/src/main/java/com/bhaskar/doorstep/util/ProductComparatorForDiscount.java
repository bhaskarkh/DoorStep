package com.bhaskar.doorstep.util;

import com.bhaskar.doorstep.model.AddressDTO;
import com.bhaskar.doorstep.model.ProductDTO;

import java.util.Comparator;

public class ProductComparatorForDiscount implements Comparator<ProductDTO> {
    @Override
    public int compare(ProductDTO o1, ProductDTO o2) {
        int a1=o1.isDiscountProduct()?1:0;
        int a2=o2.isDiscountProduct()?1:0;
        int bolReturn=a2-a1;
        if(bolReturn==0) {
            return o1.getId().compareTo(o2.getId());
        }

        return bolReturn;
    }

}
