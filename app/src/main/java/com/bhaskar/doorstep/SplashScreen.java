package com.bhaskar.doorstep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.bhaskar.doorstep.services.MySharedPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SplashScreen extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    MySharedPreferences mySharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        mySharedPreferences=new MySharedPreferences(this);
        setUserInfoInSharedPref();
    }

    private void setUserInfoInSharedPref() {
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null)
        {
            getUserDetailsFromFireBase();
            
        }else {
            startActivity(new Intent(this,LoginScreen.class));
        }

    }

    private void getUserDetailsFromFireBase() {
    }
}