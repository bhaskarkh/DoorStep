package com.bhaskar.doorstep;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bhaskar.doorstep.adapter.AddressListAdapter;
import com.bhaskar.doorstep.allinterface.SetAddresListInAdapterInterface;
import com.bhaskar.doorstep.model.AddressDTO;
import com.bhaskar.doorstep.model.ProductDTO;
import com.bhaskar.doorstep.util.AddressComparator;
import com.bhaskar.doorstep.services.AddressServices;
import com.bhaskar.doorstep.services.Home;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AddressList extends AppCompatActivity implements SetAddresListInAdapterInterface {

    ProgressBar addressListProgressBar;
    RecyclerView addresListRecycler;
    AddressListAdapter addressListAdapter;
    AddressServices addressServices;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    Intent intentForSource;
    Home home;
    ProductDTO productDTO;
    String source;
    RelativeLayout add_address_layout;
    TextView number_saved_address;
    String categorySource;
    ImageView back_btn;
    private static final String TAG = "AddressList";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        addressListProgressBar=findViewById(R.id.addressListProgressBar);
        addresListRecycler=findViewById(R.id.addresslist_recyclerview);
        add_address_layout=findViewById(R.id.add_address_layout);
        number_saved_address=findViewById(R.id.number_saved_address);
        back_btn=findViewById(R.id.addres_list_back_btn);

        intentForSource=getIntent();
        addressListProgressBar.setVisibility(View.VISIBLE);
        home=new Home(AddressList.this);
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        addressServices=new AddressServices(AddressList.this);
        addressServices.setSetAddresListInAdapterInterface(AddressList.this);

        addressServices.fetchAddressFromFirebase(firebaseDatabase,firebaseAuth);
        checkSourceInformation(intentForSource);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.backButton(intentForSource,"AddressList");
            }
        });

        add_address_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddressList.this,ChangeAddress.class);
                intent.putExtra("method","add");
                Log.d(TAG, "onClick: add_address_layout source= "+source);
                intent.putExtra("source",source);
                if(source.equals("ProductDetails"))
                {
                    intent.putExtra("selected_product",(ProductDTO)intentForSource.getParcelableExtra("selected_product"));
                    intent.putExtra("cat_name",categorySource);
                }
                startActivity(intent);
            }
        });




    }

    private void checkSourceInformation(Intent intentForSource) {
        String src=home.getSource(intentForSource);

       if(src.equals("No source found"))
       {
          source="MainActivity";

       }
       else if(src.equals("ProductDetails"))
        {
            categorySource=intentForSource.getStringExtra("cat_name");
            source="ProductDetails";
           productDTO=intentForSource.getParcelableExtra("selected_product");

        }

    }

    private void setCategoryRecycler(List<AddressDTO> addressDTOList) {
        addressListProgressBar.setVisibility(View.GONE);
        String num_saved_address=addressDTOList.size() + " saved addresses";
        number_saved_address.setText(num_saved_address);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        addresListRecycler.setLayoutManager(layoutManager);
        addresListRecycler.setItemAnimator(new DefaultItemAnimator());
        addressListAdapter=new AddressListAdapter(this,addressDTOList,intentForSource);
        addresListRecycler.setAdapter(addressListAdapter);

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void setAddresList(List<AddressDTO> addressDTOList) {
        int sizeOfList=addressDTOList.size();
        if(sizeOfList==0) {
            addressListProgressBar.setVisibility(View.GONE);
            Toast.makeText(this, "No  address Added", Toast.LENGTH_SHORT).show();
        }
        else if (sizeOfList==1)
        {
            setCategoryRecycler(addressDTOList);
        }
        else {
            addressDTOList.sort(new AddressComparator());
            setCategoryRecycler(addressDTOList);
        }



    }
}