package com.bhaskar.doorstep.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bhaskar.doorstep.EnterMobileNumber;
import com.bhaskar.doorstep.LoginScreen;
import com.bhaskar.doorstep.MainActivity;
import com.bhaskar.doorstep.R;
import com.bhaskar.doorstep.VerifyOtp;
import com.bhaskar.doorstep.allinterface.UserRegistrationDetailsInterface;
import com.bhaskar.doorstep.model.AddressDTO;
import com.bhaskar.doorstep.model.UserRegistrationDTO;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserServices {
    Context context;
    UserRegistrationDetailsInterface userRegistrationDetailsInterface;
    MySharedPreferences mySharedPreferences;
    private static final String TAG = "UsersMethod";

    public UserServices(Context context) {
        this.context = context;
        mySharedPreferences=new MySharedPreferences(context);
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

        databaseReference.addValueEventListener(new ValueEventListener() {
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
                mySharedPreferences.setUserRegistrationDetailsInterface(userRegistrationDetailsInterface);
                mySharedPreferences.saveUserDetailsToSharedPreference(userRegistrationDTO[0]);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public  void saveUserDetailsFromFirebaseToSharedPref()
    {

    }


    public void UpdateProfileToFireBase(UserRegistrationDTO userRegistrationDTO)
    {
        Log.d(TAG, "UpdateProfileToFireBase: ");


        if(userRegistrationDTO!=null)
        {
            Log.d(TAG, "UpdateProfileToFireBase: userRegistrationDTO= "+userRegistrationDTO.toString());



            DatabaseReference firebaseDatabase=FirebaseDatabase.getInstance().getReference();
            firebaseDatabase.child("test").child("user").child("user_registration").child(userRegistrationDTO.getUserId()).setValue(userRegistrationDTO)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mySharedPreferences.setUserRegistrationDetailsInterface(userRegistrationDetailsInterface);
                    mySharedPreferences.saveUserDetailsToSharedPreference(userRegistrationDTO);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Failed to save try again", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
            Log.d(TAG, "RegisterUserToFireBase: UserRegistrationDTO is null");
        }
    }

    public void CheckLoginSourceAndSignOut(String Loginsource) {
        mySharedPreferences.removeUserDetailsFromSharedPref();

        Log.d(TAG, "CheckLoginSourceAndSignOut: Loginsource= "+Loginsource);
        if(Loginsource.equals("google")) {
            Log.d(TAG, "CheckLoginSourceAndSignOut: google login");
            FirebaseAuth.getInstance().signOut();
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getResources().getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
            mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(context, "Logged out Successfully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, LoginScreen.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            });
        }
        if (Loginsource.equals("email"))
        {
            Log.d(TAG, "CheckLoginSourceAndSignOut: email login");
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(context, "Logged out Successfully", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(context, LoginScreen.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);


        }
        else {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(context, "Logged out Successfully", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(context, LoginScreen.class);
            context.startActivity(i);


        }


    }

    public void signInTheUserByCredential(PhoneAuthCredential credential,FirebaseAuth firebaseAuth,FirebaseDatabase firebaseDatabase,String phoneFromPreviousActivity) {


        firebaseAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {

                            Toast.makeText(context, "otp verified successfully Signin Success", Toast.LENGTH_SHORT).show();
                            DatabaseReference databaseReferenceForMobileUpdate = firebaseDatabase.getReference().child("test").child("user").child("user_registration").child(firebaseAuth.getCurrentUser().getUid());
                            databaseReferenceForMobileUpdate.child("rmn").setValue(phoneFromPreviousActivity);
                            databaseReferenceForMobileUpdate.child("rmnVerified").setValue(true);
                            //Update Data of Shared Reference
                            MySharedPreferences mySharedPreferences=new MySharedPreferences(context);
                            UserRegistrationDTO userRegistrationDTO=mySharedPreferences.getUserDetailsFromSharedPreference();
                            userRegistrationDTO.setRmnVerified(true);
                            userRegistrationDTO.setRmn(phoneFromPreviousActivity);
                            mySharedPreferences.saveUserDetailsToSharedPreference(userRegistrationDTO);
                            Log.d(TAG,"after updateUserMobileNumber");
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                        }
                        else
                        {


                            Log.d(TAG,"signin failed1");
                            Toast.makeText(context, "signin failed", Toast.LENGTH_SHORT).show();
                            //StyleableToast.makeText(VerifyOtp.this, "Hello World!", Toast.LENGTH_LONG, R.style.mytoast).show();
                        }

                    }
                });
    }

}
