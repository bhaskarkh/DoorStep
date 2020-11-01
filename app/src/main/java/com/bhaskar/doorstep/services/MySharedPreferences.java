package com.bhaskar.doorstep.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bhaskar.doorstep.allinterface.ProductInterface;
import com.bhaskar.doorstep.model.ProductDTO;
import com.bhaskar.doorstep.model.UserRegistrationDTO;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class MySharedPreferences {
    Context context;
    private static final String TAG = "MySharedPreferences";
    ProductInterface productInterface;

    public void setProductInterface(ProductInterface productInterface) {
        this.productInterface = productInterface;
    }

    public MySharedPreferences(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean checkSharedPrefernceExistorNot(String value)
    {
        SharedPreferences mPrefs =this.context.getSharedPreferences("userDTOsharedPrefernce",MODE_PRIVATE);
       boolean containPref= mPrefs.contains("userRegistrationDTO");
        Log.d(TAG, "checkSharedPrefernceExistorNot: containPref= "+containPref);

    return  containPref;
    }



    public void saveLoginSourceToSharedPreference(String loginSource)
    {
        Log.d(TAG, "saveLoginSourceToSharedPreference: ");
        SharedPreferences mPrefs =this.context.getSharedPreferences("loginSourcePref",MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString("loginSource", loginSource);
        prefsEditor.commit();
    }
    public String getLoginSourceToSharedPreference()
    {
        String SourceLogin="";
        SharedPreferences mPrefs =this.context.getSharedPreferences("loginSourcePref",MODE_PRIVATE);

        SourceLogin=mPrefs.getString("loginSource", "empty");
        return SourceLogin;
    }

    public void saveUserDetailsToSharedPreference(UserRegistrationDTO userRegistrationDTO)
    {
        Log.d(TAG,"inside util saveUserDetailsToSharedPreference");
        SharedPreferences mPrefs =this.context.getSharedPreferences("userDTOsharedPrefernce",MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String userRegistrationDTOjson = gson.toJson(userRegistrationDTO);
        Log.d(TAG,"userRegistrationDTOjson= "+userRegistrationDTOjson);
        prefsEditor.putString("userRegistrationDTO", userRegistrationDTOjson);
        prefsEditor.commit();

    }
    public UserRegistrationDTO getUserDetailsFromSharedPreference()
    {
        SharedPreferences mPrefs = this.context.getSharedPreferences("userDTOsharedPrefernce",MODE_PRIVATE);
        UserRegistrationDTO userRegistrationDTO=new UserRegistrationDTO();
        Gson gson = new Gson();
        String json = mPrefs.getString("userRegistrationDTO", "empty");
        userRegistrationDTO = gson.fromJson(json, UserRegistrationDTO.class);
        Log.d(TAG,"userRegistrationDTO in getSharedPref= "+userRegistrationDTO.toString());

       return userRegistrationDTO;
    }
    public  void  saveAllProductListFromFirebase(List<ProductDTO> productDTOList)
    {
        Log.d(TAG, "saveAllProductListFromFirebase: ");

        if(!checkSharedPrefExistsOrNot("productListSharedPref","productListPref")) {

            setProductListInSharedPreference(productDTOList);

        }
        else {
            Log.d(TAG, "saveAllProductListFromFirebase: already exist");
            productInterface.setProductListToRecyclerView(productDTOList);
        }


    }

    public void setProductListInSharedPreference(List<ProductDTO> productDTOList)
    {
        Log.d(TAG, "setProductListInSharedPreference: ");
        SharedPreferences mPrefs = this.context.getSharedPreferences("productListSharedPref", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String productListJson = gson.toJson(productDTOList);
        Log.d(TAG, "productListJson= " + productListJson);
        prefsEditor.putString("productListPref", productListJson);
        prefsEditor.commit();
        productInterface.setProductListToRecyclerView(productDTOList);
    }
    public List<ProductDTO> getAllProductListFromSharedPreference()
    {
        List<ProductDTO> prodList=new ArrayList<>();
        if(checkSharedPrefExistsOrNot("productListSharedPref","productListPref")) {

            SharedPreferences mPrefs = this.context.getSharedPreferences("productListSharedPref", MODE_PRIVATE);
            UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
            Gson gson = new Gson();
            String json = mPrefs.getString("productListPref", "empty");
            Type type = new TypeToken<List<ProductDTO>>() {
            }.getType();
            prodList = gson.fromJson(json, type);
            Log.d(TAG, "getAllProductListFromSharedPreference in getSharedPref= " + prodList.toString());

        }
        else {
            Log.d(TAG, "getAllProductListFromSharedPreference: sharedPref doestNot exits null ProductList Returned");
        }
        return prodList;

    }

    public void removeProductListSharedPref()
    {
        Log.d(TAG, "removeProductListSharedPref:");
        SharedPreferences mPrefs = this.context.getSharedPreferences("productListSharedPref", MODE_PRIVATE);
        mPrefs.edit().remove("productListPref").commit();
        
    }

    public boolean checkSharedPrefExistsOrNot(String getSharedText,String containsText)
    {
        SharedPreferences mPrefs = this.context.getSharedPreferences(getSharedText,MODE_PRIVATE);
        return  mPrefs.contains(containsText);
    }

    public  void changeValueofDiscountProduct(ProductDTO productDTO)
    {
        List<ProductDTO> productDTOList=getAllProductListFromSharedPreference();
        for (ProductDTO productDTO1:productDTOList)
        {

            if(productDTO1.getId().equalsIgnoreCase(productDTO.getId()))
            {
                productDTO1.setDiscountProduct(productDTO.isDiscountProduct());
                break;
            }

        }
        Log.d(TAG, "changeValueofDiscountProduct: productDTOList size= "+productDTOList.size());

        setProductListInSharedPreference(productDTOList);

    }


}
