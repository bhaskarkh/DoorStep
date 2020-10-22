package com.bhaskar.doorstep.services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bhaskar.doorstep.EnterMobileNumber;
import com.bhaskar.doorstep.LoginScreen;
import com.bhaskar.doorstep.allinterface.UserRegistrationDetailsInterface;
import com.bhaskar.doorstep.model.AddressDTO;
import com.bhaskar.doorstep.model.UserRegistrationDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UsersMethod {
    Context context;
    UserRegistrationDetailsInterface userRegistrationDetailsInterface;
    private static final String TAG = "UsersMethod";

    public UsersMethod(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setUserRegistrationDetailsInterface(UserRegistrationDetailsInterface userRegistrationDetailsInterface) {
        this.userRegistrationDetailsInterface = userRegistrationDetailsInterface;
    }

    public void getUserRegistrationFromFireBase(FirebaseDatabase firebaseDatabase, FirebaseAuth firebaseAuth)
    {
        Log.d(TAG, "getUserRegistrationFromFireBase: ");
        if (firebaseDatabase==null)
        {
            Log.d(TAG, "firebaseDatabase: null ");
        }
        else {
            Log.d(TAG, "firebaseDatabase: not null ");
        }
        if (firebaseAuth==null)
        {
            Log.d(TAG, "firebaseAuth: null ");
        }
        else {

            Log.d(TAG, "firebaseAuth: not null ");
            Log.d(TAG, " firebaseAuth.getCurrentUser().getUid()"+firebaseAuth.getCurrentUser().getUid());
        }
        final UserRegistrationDTO[] userRegistrationDTO = {new UserRegistrationDTO()};
        DatabaseReference databaseReference=firebaseDatabase.getReference().child("test").child("user").child("user_registration");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Log.d(TAG, "1st layer key= "+dataSnapshot.getKey());
                    if(dataSnapshot.getKey().equals(firebaseAuth.getCurrentUser().getUid()))
                    {
                        userRegistrationDTO[0] =dataSnapshot.getValue(UserRegistrationDTO.class);
                    }
                }
                userRegistrationDetailsInterface.saveToSharedPref(userRegistrationDTO[0]);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
