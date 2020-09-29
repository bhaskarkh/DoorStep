package com.bhaskar.doorstep.util;

import android.content.Context;

import com.bhaskar.doorstep.model.ProductDTO;
import com.bhaskar.doorstep.model.ShippingDTO;
import com.bhaskar.doorstep.model.UserRegistrationDTO;

public class ShippingServices {
    Context context;

    public ShippingServices(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ShippingDTO genrateShipping(UserRegistrationDTO userDetailsFromSharedPreference, ProductDTO selectedProduct, String orderId)
    {
        ShippingDTO shippingDTO=new ShippingDTO();




        return shippingDTO;
    }

}
