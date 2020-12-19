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
import com.bhaskar.doorstep.services.Home;
import com.bhaskar.doorstep.services.MySharedPreferences;
import com.bhaskar.doorstep.services.UserServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class EnterMobileNumber extends AppCompatActivity {

    EditText phone_number;
    Button verify_btn;
    ImageView back_btn_enter_mobile;
    private String verificationCodeBySystem;
    String phone_number_text;
    String fuid;
    FirebaseAuth firebaseAuth;
    MySharedPreferences mySharedPreferences;
    String source;
    UserServices userServices;
    FirebaseDatabase firebaseDatabase;
    ProgressBar enter_mobile_progress_bar;

    private static final String TAG = "EnterMobileNumber";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_mobile_number);
        phone_number=findViewById(R.id.phone_number_text);
        back_btn_enter_mobile=findViewById(R.id.header_back_btn_image);
        enter_mobile_progress_bar=findViewById(R.id.enter_mobile_progress_bar);
        back_btn_enter_mobile.setVisibility(View.INVISIBLE);
        phone_number.requestFocus();
        verify_btn=findViewById(R.id.verify_otp_btn);
        userServices=new UserServices(this);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
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
                enter_mobile_progress_bar.setVisibility(View.VISIBLE);
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
            sendOtpToUser(phone_number_text);
           /*Intent intent=new Intent(EnterMobileNumber.this,VerifyOtp.class);
            intent.putExtra("mobileNumber",phone_number_text);
            intent.putExtra("fuid",fuid);
            startActivity(intent);*/
        }


    }

    private void sendOtpToUser(String phoneNumber) {
        Log.d(TAG," inside sendOtpToUser phoneNumber= "+phoneNumber);
        PhoneAuthProvider phoneAuthProvider = PhoneAuthProvider.getInstance();
        phoneAuthProvider.verifyPhoneNumber(
                "+91"+phoneNumber,        // Phone number to verify
                30,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);

        Log.d(TAG, "sendOtpToUser: end");
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationCodeBySystem=s;
            Log.d(TAG,"onCodeSent verificationCodeBySystem= "+verificationCodeBySystem);

        }

        @Override
        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
            super.onCodeAutoRetrievalTimeOut(s);
            Log.d(TAG, "onCodeAutoRetrievalTimeOut: ");
            goToVerifyOtp(verificationCodeBySystem);
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            Log.d(TAG,"onVerificationCompleted callback");
            String code=phoneAuthCredential.getSmsCode();
            Log.d(TAG, "onVerificationCompleted: code= "+code);
            if(code!=null)
            {

                verifyOtp(code);
            }
            else {
                Log.d(TAG, "onVerificationCompleted: code is null");
                goToVerifyOtp(verificationCodeBySystem);
            }


        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.i(TAG,"inside onVerificationFailed");
            Log.d(TAG,"onVerificationFailed exception= "+e.getMessage());
            enter_mobile_progress_bar.setVisibility(View.INVISIBLE);
            if (e instanceof FirebaseAuthInvalidCredentialsException)
                Toast.makeText(EnterMobileNumber.this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(EnterMobileNumber.this, "verification Failed ", Toast.LENGTH_SHORT).show();

        }


    };

    private void goToVerifyOtp(String verificationCodeBySystem) {
        Intent intent=new Intent(EnterMobileNumber.this,VerifyOtp.class);
        intent.putExtra("mobileNumber",phone_number_text);
        intent.putExtra("fuid",fuid);
        intent.putExtra("verificationCodeBySystem",verificationCodeBySystem);
        startActivity(intent);

    }

    public void verifyOtp(String code) {
        Log.d(TAG,"verifyOtp verificationCodeBySystem= "+verificationCodeBySystem);
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationCodeBySystem,code);
        userServices.signInTheUserByCredential(credential,firebaseAuth,firebaseDatabase,phone_number_text);
    }


}