package com.bhaskar.doorstep.services;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bhaskar.doorstep.R;
import com.bhaskar.doorstep.allinterface.ProductInterface;
import com.bhaskar.doorstep.model.ProductDTO;
import com.bhaskar.doorstep.util.ProductComparatorForDiscount;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductServices {
    Context context;
    ProductInterface productInterface;
    MySharedPreferences mySharedPreferences;


    private static final String TAG = "ProductServices";

    public void setMySharedPreferences(Context context) {
        mySharedPreferences=new MySharedPreferences(context);
    }

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

    public void addOrRemoveBtnForDiscountProduct(ProductDTO productDTO, ImageView btnImageView)
    {
        if (productDTO!=null && productDTO.isDiscountProduct())
        {
            Glide.with(context).load(R.drawable.remove_red).into(btnImageView);
        }
        else {
            Glide.with(context).load(R.drawable.add_green).into(btnImageView);
        }

    }
    public  void getProductListByCategory(String category,FirebaseDatabase firebaseDatabase)
    {
        setMySharedPreferences(context);
        Log.d(TAG, "getProductListByCategory: ");
        if(!mySharedPreferences.checkSharedPrefExistsOrNot("productListSharedPref","productListPref"))
        {
            Log.d(TAG, "getProductListByCategory: shared Pref Doesnot Exist");
           storeProductFromFirebaseToSharedPref(firebaseDatabase);

        }
        else {
            Log.d(TAG, "getProductListByCategory: shared Pref Exit");
            List<ProductDTO> productDTOList = mySharedPreferences.getAllProductListFromSharedPreference();

            /*ProductComparatorForDiscount*/
            if (productDTOList != null) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    productDTOList.sort(new ProductComparatorForDiscount());

                }


            }

            if (category.equals("All Category")) {
                productInterface.setProductListToRecyclerView(productDTOList);

            } else {
                List<ProductDTO> pList = new ArrayList<>();
                for (ProductDTO productDTO : productDTOList) {
                    if (productDTO.getCategory().equalsIgnoreCase(category)) {
                        pList.add(productDTO);
                    }
                }
                productInterface.setProductListToRecyclerView(pList);

            }
        }
    }

    private void getProductListForCategory(String category) {

    }

    public void storeProductFromFirebaseToSharedPref(FirebaseDatabase firebaseDatabase) {
        Log.d(TAG, "storeProductFromFirebaseToSharedPref: ");

        mySharedPreferences=new MySharedPreferences(context);
        List<ProductDTO> productDTOList=new ArrayList<>();
        Log.d(TAG, "getAllProductList: ");
        DatabaseReference fireRef=firebaseDatabase.getReference();
        fireRef.child("test").child("product").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {

                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {


                        productDTOList.add( dataSnapshot1.getValue(ProductDTO.class));

                    }

                }
                mySharedPreferences.setProductInterface(productInterface);
                mySharedPreferences.saveAllProductListFromFirebase(productDTOList);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
