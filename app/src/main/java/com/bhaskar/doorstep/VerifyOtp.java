package com.bhaskar.doorstep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class VerifyOtp extends AppCompatActivity {
EditText otp_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        otp_text=findViewById(R.id.otp_text);
        otp_text.requestFocus();
    }
}