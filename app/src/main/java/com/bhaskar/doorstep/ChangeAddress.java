package com.bhaskar.doorstep;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bhaskar.doorstep.model.AddressDTO;
import com.bhaskar.doorstep.model.ProductDTO;
import com.bhaskar.doorstep.model.UserRegistrationDTO;
import com.bhaskar.doorstep.services.AddressServices;
import com.bhaskar.doorstep.services.Home;
import com.bhaskar.doorstep.services.MySharedPreferences;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChangeAddress extends AppCompatActivity implements LocationListener {
    LocationManager locationManager;
    private static final String TAG = "ChangeAddress";
    ProgressBar progressBar2;
    CardView cardView_location_btn;
    boolean firstTime = true;
    Button address_save_btn;
    CheckBox primary_address_checkbox;
    Home home;
    ProductDTO selectedProduct;
    FirebaseDatabase firebaseDatabase;
    MySharedPreferences mySharedPreferences;
    UserRegistrationDTO userRegistrationDTO;
    String method;
    Intent intentForSource;
    AddressDTO sentAddresForEdit;
    ImageView change_address_back_btn;
    TextInputLayout address_pincode, address_house_no, address_area_colony, address_city, address_state, address_user_name, address_delivery_mobile_number;

    TextInputEditText address_pincode_et, address_house_no_et, address_area_colony_et, address_city_et, address_state_et, address_user_name_et, address_delivery_mobile_number_et;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_address);
        address_pincode_et = findViewById(R.id.address_pincode_et);
        address_house_no_et = findViewById(R.id.address_house_no_et);
        address_area_colony_et = findViewById(R.id.address_area_colony_et);
        address_city_et = findViewById(R.id.address_city_et);
        address_state_et = findViewById(R.id.address_state_et);
        address_user_name_et = findViewById(R.id.address_user_name_et);
        address_delivery_mobile_number_et = findViewById(R.id.address_delivery_mobile_number_et);
        progressBar2 = findViewById(R.id.progressBar2);
        cardView_location_btn = findViewById(R.id.cardView_location_btn);
        address_save_btn=findViewById(R.id.address_save_btn);
        sentAddresForEdit=new AddressDTO();

        address_pincode=findViewById(R.id.address_pincode);
        address_house_no=findViewById(R.id.address_house_no);
        address_area_colony=findViewById(R.id.address_area_colony);
        address_city=findViewById(R.id.address_city);
        address_state=findViewById(R.id.address_state);
        address_user_name=findViewById(R.id.address_user_name);
        address_delivery_mobile_number=findViewById(R.id.address_delivery_mobile_number);
        primary_address_checkbox=findViewById(R.id.set_default_address_checkbox);
        change_address_back_btn=findViewById(R.id.change_address_back_btn);
        firebaseDatabase=FirebaseDatabase.getInstance();

        intentForSource= getIntent();
        home=new Home(this);

        change_address_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.backButton(intentForSource,"ChangeAddress");
            }
        });



        method=home.getMethod(intentForSource);

        mySharedPreferences=new MySharedPreferences(this);
        userRegistrationDTO=mySharedPreferences.getUserDetailsFromSharedPreference();
        fillDetailsInBox(intentForSource);


        
        if(userRegistrationDTO.getAddressDTOList()==null)
        {
            Log.d(TAG, "onCreate: either null");
            primary_address_checkbox.setChecked(true);
            primary_address_checkbox.setClickable(false);
        }else if(userRegistrationDTO.getAddressDTOList().size()==0)
        {
            Log.d(TAG, "only 1 address primary check box is Unclickable");
            primary_address_checkbox.setChecked(true);
            primary_address_checkbox.setClickable(false);

        }



        address_save_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if(checkMandatoryField())
                {
                    AddressDTO addressDTO=new AddressDTO();
                    addressDTO.setPincode(Integer.parseInt(address_pincode.getEditText().getText().toString()));
                    addressDTO.setHouse_no(address_house_no.getEditText().getText().toString());
                    addressDTO.setArea_colony(address_area_colony.getEditText().getText().toString());
                    addressDTO.setCity(address_city.getEditText().getText().toString());
                    addressDTO.setState(address_state.getEditText().getText().toString());
                    addressDTO.setName(address_user_name.getEditText().getText().toString());
                    addressDTO.setDeliveryMobileNo(address_delivery_mobile_number.getEditText().getText().toString());
                    if(primary_address_checkbox.isChecked())
                    {
                        addressDTO.setPrimaryAddress(true);
                    }
                    AddressServices addressServices=new AddressServices(ChangeAddress.this);
                    if(intentForSource!=null) {

                        if (method.equals("add")) {
                            addressServices.saveAddress(addressDTO, userRegistrationDTO, firebaseDatabase, intentForSource);
                        }
                        else if(method.equals("edit"))
                        {
                            addressDTO.setId(sentAddresForEdit.getId());
                            Log.d(TAG, "onClick: edit addressDTO= "+addressDTO.toString());
                            addressServices.editAddress(addressDTO, userRegistrationDTO, firebaseDatabase, intentForSource);
                        }
                    }



                }
            }
        });

        cardView_location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkGpsPermission();
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (iSLocationEnabled()) {
                    /*getCurrentLocation();*/
                    getLocation();

                } else {
                    showGPSonDialog();
                }


            }
        });


    }

    private void fillDetailsInBox(Intent intentForSource) {
        String method=intentForSource.getStringExtra("method");
        if(method!=null)
        {
            if (method.equals("edit"))
            {
                sentAddresForEdit=intentForSource.getParcelableExtra("addressVal");
                Log.d(TAG, "fillDetailsInBox: addressDTO= "+sentAddresForEdit.toString());
                setAddressValueInField(sentAddresForEdit);
            }
            if (method.equals("add"))
            {
                if (userRegistrationDTO.getRmn()!=null)
                 address_delivery_mobile_number_et.setText(userRegistrationDTO.getRmn());

                if (userRegistrationDTO.getUserName()!=null)
                address_user_name_et.setText(userRegistrationDTO.getUserName());
            }

        }
    }

    private void setAddressValueInField(AddressDTO addressDTO) {
        address_pincode_et.setText(String.valueOf(addressDTO.getPincode()));
        address_area_colony_et.setText(addressDTO.getArea_colony());
        address_city_et.setText(addressDTO.getCity());
        address_state_et.setText(addressDTO.getState());
        if (addressDTO.getDeliveryMobileNo()!=null)
            address_delivery_mobile_number_et.setText(addressDTO.getDeliveryMobileNo());
        if (addressDTO.getName()!=null)
         address_user_name_et.setText(addressDTO.getName());
        if (addressDTO.getHouse_no()!=null)
            address_house_no_et.setText(addressDTO.getHouse_no());
        if (addressDTO.isPrimaryAddress()) {
            primary_address_checkbox.setChecked(true);
            primary_address_checkbox.setClickable(false);
        }


    }


    private boolean checkMandatoryField() {
        Map<TextInputLayout,String > textInputLayoutMap=new HashMap<>();
        textInputLayoutMap.put(address_pincode,"pincode can't be empty");
        textInputLayoutMap.put(address_house_no,"House no or building name can't be empty");
        textInputLayoutMap.put(address_area_colony,"area can't be empty");
        textInputLayoutMap.put(address_city,"city can't be empty");
        textInputLayoutMap.put(address_state,"state can't be empty");
        textInputLayoutMap.put(address_user_name,"name can't be empty");
        textInputLayoutMap.put(address_delivery_mobile_number,"phone number can't be empty");
        return home.checkEmptyStringAndSetErrorMsgOfTextInputLayout(textInputLayoutMap);
    }

    private void showGPSonDialog() {
        new AlertDialog.Builder(ChangeAddress.this)
                .setTitle("Enable GPS Service")
                .setMessage("We need your GPS location to get the Address")
                .setCancelable(false)
                .setPositiveButton("Enable", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                            }
                        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressBar2.setVisibility(View.GONE);

                        Toast.makeText(ChangeAddress.this, "GPS disabled kindly type the address", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (firstTime) {
            Log.d(TAG, "onResume: first time");
            firstTime = false;

        } else {
            Log.d(TAG, "onResume: not a  first time");
            if (iSLocationEnabled()) {
                Log.d(TAG, "onResume: Gps ON");
                getLocation();
            } else {
                Log.d(TAG, "onResume: GPS OFF");
                Toast.makeText(this, "GPS is off", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void checkGpsPermission() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }


    }

    private boolean iSLocationEnabled() {
        Log.d(TAG, "locationEnabled:");
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gps_enabled && !network_enabled) {
            Log.d(TAG, "locationEnabled: gps of");
            return false;
        } else {
            Log.d(TAG, "locationEnabled: gps on");

            return true;
        }

    }

    void getLocation() {
        Log.d(TAG, "getLocation: ");
        progressBar2.setVisibility(View.VISIBLE);
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            assert locationManager != null;
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 5, (LocationListener) this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onRequestPermissionsResult: Permission Granted");

            } else {
                Log.d(TAG, "onRequestPermissionsResult: else permisson not granted");
            }
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            Log.d(TAG,"address value= "+addresses.get(0).toString());
            progressBar2.setVisibility(View.GONE);
            AddressDTO addressDTO=new AddressDTO();
            addressDTO.setPincode(Integer.parseInt(addresses.get(0).getPostalCode()));
            addressDTO.setArea_colony(addresses.get(0).getFeatureName()+","+addresses.get(0).getLocality());
            addressDTO.setCity(addresses.get(0).getLocality());
            addressDTO.setState(addresses.get(0).getAdminArea());
            setAddressValueInField(addressDTO);




        } catch (Exception e) {
        }
        finally {
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    @Override
    public void onPause() {
       locationManager= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(this);
        super.onPause();
    }
}