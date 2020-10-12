package com.bhaskar.doorstep.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.bhaskar.doorstep.model.UserRegistrationDTO;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class MySharedPreferences {
    Context context;
    private static final String TAG = "MySharedPreferences";

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
}
