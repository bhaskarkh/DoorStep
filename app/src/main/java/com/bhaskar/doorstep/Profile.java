package com.bhaskar.doorstep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bhaskar.doorstep.allinterface.UserRegistrationDetailsInterface;
import com.bhaskar.doorstep.model.AddressDTO;
import com.bhaskar.doorstep.model.UserRegistrationDTO;
import com.bhaskar.doorstep.services.AddressServices;
import com.bhaskar.doorstep.services.Home;
import com.bhaskar.doorstep.services.MySharedPreferences;
import com.bhaskar.doorstep.services.UserServices;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class Profile extends AppCompatActivity implements UserRegistrationDetailsInterface {

    MySharedPreferences mySharedPreferences;
    UserRegistrationDTO userRegistrationDTO;
    Home home;
    AddressServices addressServices;
    ImageView profile_image, profile_edit_btn,header_back_btn_image;
    TextView header_title, profile_mobile, profile_email, profile_address_full, profile_address_view_more, profile_view_all_order;
    ConstraintLayout header_layout_color;
    EditText profile_name;
    UserServices userServices;
    boolean isEditBtn=true;
    Button profile_update_rmn;

    private static final String TAG = "Profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mySharedPreferences = new MySharedPreferences(this);
        home=new Home(this);
        addressServices=new AddressServices(this);
        userRegistrationDTO = mySharedPreferences.getUserDetailsFromSharedPreference();
        profile_update_rmn=findViewById(R.id.profile_update_rmn);
        profile_update_rmn.setVisibility(View.INVISIBLE);
        profile_name = findViewById(R.id.profile_name);
        profile_mobile = findViewById(R.id.profile_mobile);
        profile_email = findViewById(R.id.profile_email);
        profile_address_full = findViewById(R.id.profile_address_full);
        profile_address_view_more = findViewById(R.id.profile_address_view_more);
        profile_view_all_order = findViewById(R.id.profile_view_all_order);
        profile_image = findViewById(R.id.profile_image);
        profile_edit_btn = findViewById(R.id.header_profile_pic);
        header_back_btn_image=findViewById(R.id.header_back_btn_image);
        header_layout_color=findViewById(R.id.header_constraint_layout);
        header_layout_color.setBackgroundResource(R.color.icons);
        header_back_btn_image.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        header_title=findViewById(R.id.header_title);
        header_title.setVisibility(View.INVISIBLE);
        header_back_btn_image.setVisibility(View.INVISIBLE);

        profile_edit_btn.setVisibility(View.VISIBLE);
        profile_edit_btn.setImageResource(R.drawable.ic_edit);
        profile_name.setEnabled(false);
        profile_name.setFocusable(false);
        userServices=new UserServices(this);
        userServices.setUserRegistrationDetailsInterface(this);



        setValueInProfilePage();
        profile_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditBtn) {
                    //editedable mode
                    isEditBtn=false;
                    profile_update_rmn.setVisibility(View.VISIBLE);
                    profile_edit_btn.setImageResource(R.drawable.ic_baseline_save_24);
                    profile_name.setEnabled(true);
                    profile_name.setFocusableInTouchMode(true);
                    profile_name.setTextColor(getResources().getColor(R.color.primary));
                    profile_name.setTypeface(Typeface.DEFAULT_BOLD);
                    profile_name.setSelection(profile_name.getText().length());
                    profile_name.requestFocus();

                }else
                {//save clicked
                    profile_update_rmn.setVisibility(View.INVISIBLE);
                    isEditBtn=true;
                    profile_edit_btn.setImageResource(R.drawable.ic_edit);
                    profile_name.setEnabled(false);
                    profile_name.setFocusable(false);
                    profile_name.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    profile_name.setTypeface(Typeface.DEFAULT);
                    profile_name.setTextColor(getResources().getColor(R.color.black));
                    userRegistrationDTO.setUserName(profile_name.getText().toString());
                    saveProfileDeatils(userRegistrationDTO);
                }

            }
        });
        profile_update_rmn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Profile.this,EnterMobileNumber.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("source_to_edit_number","profile");
                startActivity(intent);
            }
        });

        profile_view_all_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this,YourOrder.class));
            }
        });
        profile_address_view_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Profile.this,AddressList.class);
                intent.putExtra("source","profile");
                intent.putExtra("source_to_product_details","profile");

                startActivity(intent);
            }
        });
        header_back_btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.gotToHome();
            }
        });

    }

    private void saveProfileDeatils(UserRegistrationDTO userRegistrationDTO) {
        userServices.UpdateProfileToFireBase(userRegistrationDTO);
    }

    private void setValueInProfilePage() {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_no_image_available)
                .error(R.drawable.ic_no_image_available)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.NORMAL);

        Glide.with(this).load(userRegistrationDTO.getUserPhoto())
                .circleCrop()
                .apply(options)
                .into(profile_image);

        profile_name.setText(userRegistrationDTO.getUserName());
        if (userRegistrationDTO.getRmn()!=null && !userRegistrationDTO.getRmn().isEmpty()) {
            profile_mobile.setText(userRegistrationDTO.getRmn());
        }
        else {
            profile_mobile.setText("No mobile number saved");
            profile_mobile.setTextSize(12);
            profile_update_rmn.setVisibility(View.VISIBLE);
        }
        profile_email.setText(userRegistrationDTO.getUserEmail());
        if (userRegistrationDTO.getAddressDTOList()!=null && userRegistrationDTO.getAddressDTOList().size()!=0)
        {
            AddressDTO addressDTO=addressServices.getPrimaryAddress(userRegistrationDTO.getAddressDTOList());
            String firstAddress=addressServices.getAddressWithoutPinAndMobile(addressDTO);
            String pincode=String.valueOf(addressDTO.getPincode());
            profile_address_full.setText(firstAddress+","+pincode);
            int addressSize=userRegistrationDTO.getAddressDTOList().size()-1;
            if(addressSize==0)
            {
                profile_address_view_more.setText("Add  More Address");
            }
            else {
                profile_address_view_more.setText("View "+addressSize+" More");
            }

        }
        else {
            profile_address_full.setText("No Address Saved");
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        home.gotToHome();
    }

    @Override
    public void saveToSharedPref(UserRegistrationDTO userRegistrationDTO) {
        Toast.makeText(this, "Profile Saved Successfully", Toast.LENGTH_SHORT).show();
    }
}