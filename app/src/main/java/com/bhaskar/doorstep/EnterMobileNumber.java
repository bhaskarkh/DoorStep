package com.bhaskar.doorstep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class EnterMobileNumber extends AppCompatActivity {

    EditText phone_number;
    Button verify_btn;

    String phone_number_text;

    private static final String TAG = "EnterMobileNumber";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_mobile_number);
        phone_number=findViewById(R.id.phone_number_text);
        phone_number.requestFocus();
        verify_btn=findViewById(R.id.verify_otp_btn);


        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Before Validation");
                submitOtpValidation();

            }


        });


    }
    private void submitOtpValidation() {

        phone_number_text=phone_number.getText().toString();

        if(phone_number_text.length()!=10)
        {
            phone_number.setError("Please Eneter Valid Mobile Number");
            phone_number.requestFocus();
            return;
        }
        else {


            Log.d(TAG,"Valid mobile number= "+phone_number_text);
            Intent intent=new Intent(EnterMobileNumber.this,VerifyOtp.class);
            intent.putExtra("mobileNumber",phone_number_text);
            startActivity(intent);
           // sendOtpToUser(phone_number_text);

        }


    }



}