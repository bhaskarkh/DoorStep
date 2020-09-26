package com.bhaskar.doorstep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.AllianceLoader;
import com.bhaskar.doorstep.model.GoogleSignInDTO;
import com.bhaskar.doorstep.model.UserRegistrationDTO;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginScreen extends AppCompatActivity {

    private static final int RC_SIGN_IN =123 ;
    private GoogleSignInClient mGoogleSignInClient;
    SignInButton signInButton;
    private String TAG="LoginScreen";
    private FirebaseAuth mAuth;
    ProgressBar loginProgressBar;
    //FirebaseFirestore firebaseFirestore;
    FirebaseDatabase database;
    DatabaseReference databaseReference;


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null)
        {
         Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        signInButton = findViewById(R.id.google_sign_in_button);
        loginProgressBar=findViewById(R.id.login_progress_bar);
        signInButton.setSize(SignInButton.SIZE_STANDARD);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        mAuth=FirebaseAuth.getInstance();

        database=FirebaseDatabase.getInstance();
        String db_mode=getString(R.string.db_mode);
        Log.d(TAG,"db_mode= "+db_mode);
        databaseReference=database.getReference().child(db_mode);
        createGoogleSignInRequest();
    }

    private void createGoogleSignInRequest() {
// Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
          mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    private void signIn() {
       Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle: success account id" + account.getId());
               loginProgressBar.setVisibility(View.VISIBLE);

                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.d(TAG, "Google sign in failed message= "+e.getMessage(), e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();


                            Log.d(TAG, "user= "+user.getPhoneNumber());
                            loginProgressBar.setVisibility(View.GONE);
                            Boolean isNewUser=task.getResult().getAdditionalUserInfo().isNewUser();
                           Log.d(TAG,"isnewUser= "+isNewUser);
                            if(isNewUser) {
                                Log.d(TAG,"New User");
                                RegisterUserInFireBase(user);
                            }
                            else
                            {
                                Log.d(TAG,"Old User");
                                Intent intent=new Intent(LoginScreen.this, MainActivity.class);
                                startActivity(intent);
                            }

                          //  updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginScreen.this, " signInWithCredential:failure exception= "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            Log.d(TAG, "signInWithCredential:failure", task.getException());

                          //  updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void RegisterUserInFireBase(final FirebaseUser fuser) {
        Log.d(TAG,"inside RegisterUserInFireBase");
        final UserRegistrationDTO userRegistrationDTO=getUserDetailFromGoogle(fuser);
        if(userRegistrationDTO!=null)
        {
            databaseReference.child("user").child("user_registration").child(fuser.getUid()).setValue(userRegistrationDTO);
            Intent intent=new Intent(LoginScreen.this, EnterMobileNumber.class);
            startActivity(intent);

        }

    }

    private void LoadProgressBar(AllianceLoader aa)
{

    AllianceLoader   allianceLoader1  = new AllianceLoader(
            this,
            40,
            6,
            true,
            10,
            ContextCompat.getColor(this, R.color.red),
            ContextCompat.getColor(this, R.color.amber),
            ContextCompat.getColor(this, R.color.green));

    allianceLoader1.setAnimDuration(500);

    aa.addView(allianceLoader1);
}

    public UserRegistrationDTO getUserDetailFromGoogle(FirebaseUser fuser)
    {
        UserRegistrationDTO userRegistrationDTO=new UserRegistrationDTO();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(LoginScreen.this);
        if (acct != null) {

            
            userRegistrationDTO.setUserName(acct.getDisplayName());
            userRegistrationDTO.setUserEmail(acct.getEmail());
            userRegistrationDTO.setUserId(fuser.getUid());
            userRegistrationDTO.setUserPhoto(acct.getPhotoUrl().toString());
            userRegistrationDTO.setRole("user");
            userRegistrationDTO.setRmn("");
            userRegistrationDTO.setRmnVerified(false);
            userRegistrationDTO.setUserRegisteredDate(getCurrentDateAndTime());
            userRegistrationDTO.setUserStatus("Active");


        }
        Log.d(TAG,"getUserDetailFromGoogle userRegistrationDTO= "+userRegistrationDTO.toString());

        return userRegistrationDTO;
    }

    private String getCurrentDateAndTime() {
        String date="";
        String pattern = "dd-MM-yyyy-HH-mm-ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
         date= simpleDateFormat.format(new Date());
        Log.d(TAG,"current date and time"+date);

        return date;
    }
}