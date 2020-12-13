package com.bhaskar.doorstep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bhaskar.doorstep.model.UserRegistrationDTO;
import com.bhaskar.doorstep.services.MySharedPreferences;
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
    ImageView back_btn_enter_mobile;

    String phone_number_text;
    String fuid;
    FirebaseAuth firebaseAuth;
    MySharedPreferences mySharedPreferences;
    String source;

    private static final String TAG = "EnterMobileNumber";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_mobile_number);
        phone_number=findViewById(R.id.phone_number_text);
        back_btn_enter_mobile=findViewById(R.id.header_back_btn_image);
        back_btn_enter_mobile.setVisibility(View.INVISIBLE);
        phone_number.requestFocus();
        verify_btn=findViewById(R.id.verify_otp_btn);
        firebaseAuth=FirebaseAuth.getInstance();
        fuid=firebaseAuth.getCurrentUser().getUid();
        mySharedPreferences=new MySharedPreferences(this);
        Log.d(TAG,"fuid in entermobile= "+fuid);
        /*source_to_edit_number*/
        source=getIntent().getStringExtra("source_to_edit_number");
        back_btn_enter_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EnterMobileNumber.this,Profile.class));
            }
        });

        if (source!=null&&source.equalsIgnoreCase("profile"))
        {
            back_btn_enter_mobile.setVisibility(View.VISIBLE);
        }

        UserRegistrationDTO userRegistrationDTO=mySharedPreferences.getUserDetailsFromSharedPreference();
        if (userRegistrationDTO!=null)
        {
            if(userRegistrationDTO.getRmn()!=null)
             phone_number.setText(userRegistrationDTO.getRmn());
        }
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
            intent.putExtra("fuid",fuid);
            startActivity(intent);
           // sendOtpToUser(phone_number_text);

        }


    }



}