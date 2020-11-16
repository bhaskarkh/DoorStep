package com.bhaskar.doorstep.services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bhaskar.doorstep.AddressList;
import com.bhaskar.doorstep.MainActivity;
import com.bhaskar.doorstep.R;
import com.bhaskar.doorstep.allinterface.SetAddresListInAdapterInterface;
import com.bhaskar.doorstep.model.AddressDTO;
import com.bhaskar.doorstep.model.ProductDTO;
import com.bhaskar.doorstep.model.UserRegistrationDTO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddressServices {
    Context context;
    SetAddresListInAdapterInterface setAddresListInAdapterInterface;
    private static final String TAG = "AddressServices";

    public AddressServices(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setSetAddresListInAdapterInterface(SetAddresListInAdapterInterface setAddresListInAdapterInterface) {
        this.setAddresListInAdapterInterface = setAddresListInAdapterInterface;
    }

    public String firstLineAddress(AddressDTO addressDTO)
    {
        String str="No First Line";
        if(addressDTO!=null) {
            str = addressDTO.getHouse_no() + "," + addressDTO.getArea_colony();
        }


      return  str;
    }
    public String secondLineAddress(AddressDTO addressDTO)
    {
        String str="No First Line";
        if(addressDTO!=null) {
            str = addressDTO.getCity() + "," + addressDTO.getState() + "-" + addressDTO.getPincode();
        }


        return  str;
    }
    public String getAddressWithoutPinAndMobile(AddressDTO addressDTO)
    {
        String str="No Address Founf";
        if (addressDTO!=null)
        {
            str=firstLineAddress(addressDTO)+","+addressDTO.getCity()+","+addressDTO.getState();
        }

        return str;
    }

    public String getFullAddress(AddressDTO addressDTO)
    {
        String str="Failed to load Address";
        if(addressDTO!=null)
        {
            str=firstLineAddress(addressDTO)+","+secondLineAddress(addressDTO)+"("+addressDTO.getDeliveryMobileNo()+")";
        }
        else {
            Log.d(TAG, "getFullAddress: addressDto is null");
        }
        return str;
    }

    public AddressDTO getPrimaryAddress(List<AddressDTO> addressDTOList)
    {
        AddressDTO addressDTO=new AddressDTO();
        for (AddressDTO a:addressDTOList)
        {
            if(a.isPrimaryAddress())
            {
                return a;
            }
        }


        return addressDTO;
    }

    public void saveAddress(AddressDTO addressDTO, UserRegistrationDTO userRegistrationDTO, FirebaseDatabase firebaseDatabase,Intent intentForSource){
        if (userRegistrationDTO.getAddressDTOList()!=null) {
            if (userRegistrationDTO.getAddressDTOList().size() == 0) {
                addressDTO.setId(1);
                addressDTO.setPrimaryAddress(true);
               save(addressDTO,userRegistrationDTO,firebaseDatabase,intentForSource);

            } else {
                addressDTO.setId(userRegistrationDTO.getAddressDTOList().size()+1);
                save(addressDTO,userRegistrationDTO,firebaseDatabase,intentForSource);


            }
        }else {
            addressDTO.setId(1);
            addressDTO.setPrimaryAddress(true);
            save(addressDTO,userRegistrationDTO,firebaseDatabase,intentForSource);
        }


    }

    public void editAddress(AddressDTO addressDTO, UserRegistrationDTO userRegistrationDTO, FirebaseDatabase firebaseDatabase,Intent intentForSource)
    {
        Log.d(TAG, "editAddress: addressDTO= "+addressDTO.toString());
       List<AddressDTO> addressDTOList=userRegistrationDTO.getAddressDTOList();
       if(addressDTO.isPrimaryAddress())
       {
           addressDTOList=setALlAdressPrimaryFalse(addressDTOList);
       }
       int i=0;
        Log.d(TAG, "editAddress: addressList before"+addressDTOList.toString());
       for(AddressDTO addressDTO1:addressDTOList)
       {

           Log.d(TAG, "editAddress: addressDTO1= "+addressDTO1.getId()+" addressDTO= "+addressDTO.getId());
           if(addressDTO1.getId()==addressDTO.getId())
           {
               Log.d(TAG, "editAddress: i= "+i);
            addressDTOList.set(i,addressDTO);

           }

           i++;

       }

        Log.d(TAG, "editAddress: addressList After"+addressDTOList.toString());

        userRegistrationDTO.setAddressDTOList(addressDTOList);

        addAddressInFirebase(userRegistrationDTO,firebaseDatabase,intentForSource);


    }


    private void save(AddressDTO addressDTO,UserRegistrationDTO userRegistrationDTO,FirebaseDatabase firebaseDatabase,Intent intentForSource) {
        List<AddressDTO> addressDTOList;
       if(userRegistrationDTO.getAddressDTOList()!=null) {
           addressDTOList = userRegistrationDTO.getAddressDTOList();
           if (addressDTO.isPrimaryAddress())
           {
               addressDTOList=setALlAdressPrimaryFalse(addressDTOList);
           }
       }
       else {
          addressDTOList=new ArrayList<>();
       }
        addressDTOList.add(addressDTO);
        userRegistrationDTO.setAddressDTOList(addressDTOList);

        addAddressInFirebase(userRegistrationDTO,firebaseDatabase,intentForSource);




    }

    private List<AddressDTO>  setALlAdressPrimaryFalse(List<AddressDTO> addressDTOList) {
        for (AddressDTO addressDTO:addressDTOList)
        {
            addressDTO.setPrimaryAddress(false);
        }
        return addressDTOList;
    }

    public void fetchAddressFromFirebase(FirebaseDatabase firebaseDatabase, FirebaseAuth firebaseAuth)
    {
        List<AddressDTO> addressDTOList=new ArrayList<>();
        Log.d(TAG, "fetchAddressFromFirebase: ");
        DatabaseReference databaseReference=firebaseDatabase.getReference().child("test").child("user").child("user_registration");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Log.d(TAG, "1st layer key= "+dataSnapshot.getKey());
                    if(dataSnapshot.getKey().equals(firebaseAuth.getCurrentUser().getUid()))
                    {
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                        {
                            Log.d(TAG, "2nd layer key= "+dataSnapshot1.getKey());
                            if(dataSnapshot1.getKey().equals("addressDTOList"))
                            {
                                for (DataSnapshot dataSnapshot2:dataSnapshot1.getChildren())
                                {
                                    Log.d(TAG, "3rd layer key= "+dataSnapshot2.getKey());
                                    AddressDTO addressDTO=dataSnapshot2.getValue(AddressDTO.class);
                                    addressDTOList.add(addressDTO);
                                }

                            }

                        }
                    }
                }
                setAddresListInAdapterInterface.setAddresList(addressDTOList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }



    public  void addAddressInFirebase(UserRegistrationDTO userRegistrationDTO, FirebaseDatabase firebaseDatabase,Intent intentForSource){
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        Log.d(TAG,"inside RegisterUserInFireBase");

        if(userRegistrationDTO!=null)
        {
            databaseReference.child("test").child("user").child("user_registration").child(userRegistrationDTO.getUserId()).setValue(userRegistrationDTO).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "onSuccess: saved address and sharedPreference changed ");
                    MySharedPreferences mySharedPreferences=new MySharedPreferences(context);
                    mySharedPreferences.saveUserDetailsToSharedPreference(userRegistrationDTO);


                    String source=intentForSource.getStringExtra("source");
                        if(source!=null)
                        {
                            Log.d(TAG, "onSuccess: source"+source);
                            if (source.equals("ProductDetails"))
                            {
                                Intent intent=new Intent(context, AddressList.class);
                                ProductDTO productDTO=intentForSource.getParcelableExtra("selected_product");
                                intent.putExtra("selected_product",productDTO);
                                intent.putExtra("source","ProductDetails");
                                intent.putExtra("cat_name",intentForSource.getStringExtra("cat_name"));
                                intent.putExtra("source_to_product_details",intentForSource.getStringExtra("source_to_product_details"));
                                context.startActivity(intent);
                            }

                        }
                        else {
                            Log.d(TAG, "No source found to redirect going to main Activity");
                            context.startActivity(new Intent(context, MainActivity.class));
                        }




                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Failed To save Address Try Again", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Failed To save Address exceptipn msg= "+e.getMessage());
                }
            });


        }

    }

    public void getCustomAddressPinMobileAndAddress(TextView order_status_change_pincode, TextView order_status_change_mobile, TextView order_dashboard_address_full,AddressDTO addressDTO) {
       Home home=new Home(context);
       String full_address=getAddressWithoutPinAndMobile(addressDTO);
       order_dashboard_address_full.setText(full_address);
       if(full_address.length()<=60)
       {
           order_dashboard_address_full.setTextSize(home.pixelsToSp(context.getResources().getDimension(R.dimen.text16sp)));
       }
       else if(full_address.length()>60 &&full_address.length()<=100)
        {
            order_dashboard_address_full.setTextSize(home.pixelsToSp(context.getResources().getDimension(R.dimen.text14sp)));
        }
       else if(full_address.length()>100 &&full_address.length()<=180)
       {
           order_dashboard_address_full.setTextSize(home.pixelsToSp(context.getResources().getDimension(R.dimen.text12sp)));

       }
       else {

           home.limitCharacterInView(order_dashboard_address_full,165,full_address);
           order_dashboard_address_full.setTextSize(home.pixelsToSp(context.getResources().getDimension(R.dimen.text12sp)));
       }

        order_status_change_pincode.setText("Pincode: "+addressDTO.getPincode());
       order_status_change_mobile.setText(addressDTO.getDeliveryMobileNo());

    }
}
