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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyOtp extends AppCompatActivity {
   EditText otp_text;
   Button verify_otp_btn;
    private String verificationCodeBySystem;
    TextView mobile_number_in_verify;
    private static final String TAG = "VerifyOtp";
    ProgressBar otp_verify_progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        otp_text=findViewById(R.id.otp_text);
        verify_otp_btn=findViewById(R.id.verify_otp_btn);
        mobile_number_in_verify=findViewById(R.id.mobile_number_in_verify);
        otp_verify_progressBar=findViewById(R.id.otp_verify_progressBar);

        otp_text.requestFocus();
        final String phoneFromPreviousActivity=getIntent().getStringExtra("mobileNumber");

        if(phoneFromPreviousActivity!=null) {
            mobile_number_in_verify.setText(phoneFromPreviousActivity);
            sendOtpToUser(phoneFromPreviousActivity);

        }

        verify_otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phoneFromPreviousActivity!=null) {
                    String otp_text_value=otp_text.getText().toString();

                    if(otp_text_value.isEmpty()||otp_text_value.length()<6)
                    {
                        otp_text.setError("Wrong Otp");
                        otp_text.requestFocus();
                        return;
                    }

                    otp_verify_progressBar.setVisibility(View.VISIBLE);
                    verifyOtp(otp_text_value);


                }

            }
        });




    }

    private void sendOtpToUser(String phoneNumber) {
        Log.d(TAG," inside sendOtpToUser phoneNumber= "+phoneNumber);

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Log.d(TAG,"onCodeSent ");
            verificationCodeBySystem=s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            Log.d(TAG,"onVerificationCompleted callback");
            String code=phoneAuthCredential.getSmsCode();
            if(code!=null)
            {

                verifyOtp(code);
            }


        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerifyOtp.this, "verification Failed ", Toast.LENGTH_SHORT).show();
            Log.d(TAG,"onVerificationFailed exception= "+e.getLocalizedMessage());


        }
    };

    public void verifyOtp(String code) {
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationCodeBySystem,code);
        signInTheUserByCredential(credential);
    }

    private void signInTheUserByCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyOtp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            otp_verify_progressBar.setVisibility(View.GONE);
                            Toast.makeText(VerifyOtp.this, "Signin Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else
                        {
                            otp_verify_progressBar.setVisibility(View.GONE);
                            Toast.makeText(VerifyOtp.this, "signin failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}