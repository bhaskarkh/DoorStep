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
import android.widget.TextView;
import android.widget.Toast;

import com.bhaskar.doorstep.model.UserRegistrationDTO;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
/*import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;*/

import java.util.concurrent.TimeUnit;

public class VerifyOtp extends AppCompatActivity {
   EditText otp_text;
   Button verify_otp_btn;
   ImageView back_verify_otp_btn;
    private String verificationCodeBySystem;
    TextView mobile_number_in_verify;
    private static final String TAG = "VerifyOtp";
    ProgressBar otp_verify_progressBar;
  //  FirebaseFirestore firebaseFirestore;
    String phoneFromPreviousActivity,fuid;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    UserServices userServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        back_verify_otp_btn=findViewById(R.id.back_verify_otp_btn);
        otp_text=findViewById(R.id.otp_text);
        verify_otp_btn=findViewById(R.id.verify_otp_btn);
        mobile_number_in_verify=findViewById(R.id.mobile_number_in_verify);
        otp_verify_progressBar=findViewById(R.id.otp_verify_progressBar);
        //firebaseFirestore=FirebaseFirestore.getInstance();
        userServices=new UserServices(this);
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        Log.d(TAG,"verify otp firebaseAuth uid= "+firebaseAuth.getCurrentUser().getUid());
        otp_text.requestFocus();
        phoneFromPreviousActivity=getIntent().getStringExtra("mobileNumber");
        fuid=getIntent().getStringExtra("fuid");
        verificationCodeBySystem=getIntent().getStringExtra("verificationCodeBySystem");

        if(phoneFromPreviousActivity!=null) {
            mobile_number_in_verify.setText(phoneFromPreviousActivity);
           // sendOtpToUser(phoneFromPreviousActivity);

        }

        back_verify_otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VerifyOtp.this,EnterMobileNumber.class));
            }
        });
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
            Log.d(TAG,"onCodeSent");
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
            Log.i(TAG,"inside onVerificationFailed");
            Log.d(TAG,"onVerificationFailed exception= "+e.getMessage());
            if (e instanceof FirebaseAuthInvalidCredentialsException)
            Toast.makeText(VerifyOtp.this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(VerifyOtp.this, "verification Failed ", Toast.LENGTH_SHORT).show();

        }


    };

    public void verifyOtp(String code) {
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationCodeBySystem,code);
        userServices.signInTheUserByCredential(credential,firebaseAuth,firebaseDatabase,phoneFromPreviousActivity);
    }

    private void signInTheUserByCredential(PhoneAuthCredential credential) {


        firebaseAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(VerifyOtp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            otp_verify_progressBar.setVisibility(View.GONE);
                            Toast.makeText(VerifyOtp.this, "otp verified successfully Signin Success", Toast.LENGTH_SHORT).show();
                            Log.d(TAG,"firebaseAuth.getCurrentUser().getUid()= "+firebaseAuth.getCurrentUser().getUid()+" fuid= "+fuid);

                            DatabaseReference databaseReferenceForMobileUpdate = firebaseDatabase.getReference().child("test").child("user").child("user_registration").child(firebaseAuth.getCurrentUser().getUid());
                            databaseReferenceForMobileUpdate.child("rmn").setValue(phoneFromPreviousActivity);
                            databaseReferenceForMobileUpdate.child("rmnVerified").setValue(true);
                            //Update Data of Shared Reference
                            MySharedPreferences mySharedPreferences=new MySharedPreferences(VerifyOtp.this);
                            UserRegistrationDTO userRegistrationDTO=mySharedPreferences.getUserDetailsFromSharedPreference();
                            userRegistrationDTO.setRmnVerified(true);
                            userRegistrationDTO.setRmn(phoneFromPreviousActivity);
                            mySharedPreferences.saveUserDetailsToSharedPreference(userRegistrationDTO);
                            Log.d(TAG,"after updateUserMobileNumber");
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else
                        {
                            otp_verify_progressBar.setVisibility(View.GONE);

                            Log.d(TAG,"signin failed1");
                          Toast.makeText(VerifyOtp.this, "signin failed", Toast.LENGTH_SHORT).show();
                            //StyleableToast.makeText(VerifyOtp.this, "Hello World!", Toast.LENGTH_LONG, R.style.mytoast).show();
                        }

                    }
                });
    }

 /*   private void updateUserMobileNumber(DocumentReference documentReference) {
        Log.d(TAG,"inside updateUserMobileNumber");

        if(phoneFromPreviousActivity!=null)
        {
            documentReference.update("rmn",phoneFromPreviousActivity,"rmnVerified",true).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG,"RMN verified and Updated ");
                    Toast.makeText(VerifyOtp.this, "Mobile Number Verified Successfull", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG,"RMN verification failed error msg= "+e.getMessage());
                    Toast.makeText(VerifyOtp.this, "Mobile Number Verification Failed Try Again", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }*/
}