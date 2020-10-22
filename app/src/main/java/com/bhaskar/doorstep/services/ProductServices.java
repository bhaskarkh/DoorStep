package com.bhaskar.doorstep.services;

import android.content.Context;
import android.util.Log;

import android.widget.Toast;

import androidx.annotation.NonNull;
import com.bhaskar.doorstep.allinterface.ProductInterface;
import com.bhaskar.doorstep.model.ProductDTO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

public class ProductServices {
    Context context;
    ProductInterface productInterface;
    private static final String TAG = "ProductServices";

    public ProductServices(Context context) {
        this.context = context;
    }

    public void setProductInterface(ProductInterface productInterface) {
        this.productInterface = productInterface;
    }

    public void addProductInDb(ProductDTO productDTO, FirebaseDatabase firebaseDatabase) {
        Log.d(TAG,"inside addInDb");

        firebaseDatabase.getReference().child("test").child("product").child(productDTO.getCategory()).child(productDTO.getName()).setValue(productDTO).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG,"onSuccess firebase added");
                productInterface.onProductAddedinDb();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"failure exception msg= "+e.getMessage());

                productInterface.onProductAddFailed();
            }
        });
    }


}
