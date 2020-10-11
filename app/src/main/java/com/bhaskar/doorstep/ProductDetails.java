package com.bhaskar.doorstep;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bhaskar.doorstep.model.AddressDTO;
import com.bhaskar.doorstep.model.ProductDTO;
import com.bhaskar.doorstep.model.UserRegistrationDTO;
import com.bhaskar.doorstep.util.AddressServices;
import com.bhaskar.doorstep.util.Home;
import com.bhaskar.doorstep.util.MySharedPreferences;
import com.bhaskar.doorstep.allinterface.OnOrderSubmissionListener;
import com.bhaskar.doorstep.util.OrderDetailsServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class ProductDetails extends AppCompatActivity implements OnOrderSubmissionListener {

    ImageView img, back;
    TextView proName, proPrice, proDesc, proQty, proUnit;
    TextView address_userName,address_pincode,address_full_address,deliver_to_text;
    MySharedPreferences mySharedPreferences;
    AddressServices addressServices;
    Home home;

    String name, price, desc, qty, unit;

    int image;
    Button buy_btn,change_address_button;
    ProductDTO selectedProduct;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    ProgressBar place_order_progressbar;
    final String TAG="ProductDetails";
    OnOrderSubmissionListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        proName = findViewById(R.id.productName);
        proDesc = findViewById(R.id.prodDesc);
        proPrice = findViewById(R.id.prodPrice_new);
        img = findViewById(R.id.big_image);
        back = findViewById(R.id.back2);
        change_address_button=findViewById(R.id.change_address_button);
        address_userName=findViewById(R.id.prod_address_user_name);
        address_pincode=findViewById(R.id.prod_address_pincode);
        address_full_address=findViewById(R.id.prod_address_full_address);
        deliver_to_text=findViewById(R.id.prod_deliver_to_text);
        buy_btn=findViewById(R.id.buy_btn);
        place_order_progressbar=findViewById(R.id.place_order_progressbar);
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        mySharedPreferences=new MySharedPreferences(ProductDetails.this);
        addressServices=new AddressServices(ProductDetails.this);
        home=new Home(ProductDetails.this);

        Intent i = getIntent();
        selectedProduct=i.getParcelableExtra("selected_product");
          /*  i.putExtra("name", productDTOList.get(position).getName());
                i.putExtra("image", productDTOList.get(position).getImage());
                i.putExtra("price",productDTOList.get(position).getPrice());
                i.putExtra("desc",productDTOList.get(position).getDescription());
                i.putExtra("qty",productDTOList.get(position).getAmountAvailable());
                i.putExtra("unit",productDTOList.get(position).getQuantityType());*/

         name = selectedProduct.getName();
         price = String.valueOf(selectedProduct.getPrice());
         desc = selectedProduct.getDescription();
        /* qty = String.valueOf(selectedProduct.getQuantity());
         unit = selectedProduct.getQuantityType();*/


         proName.setText(name);
         proPrice.setText(price);
         proDesc.setText(desc);
        /* proQty.setText(qty);
         proUnit.setText(unit);*/


        img.setImageResource(R.drawable.pd);
        setValueInAddress(address_userName,address_pincode,address_full_address,deliver_to_text);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ProductDetails.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        });

        buy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                place_order_progressbar.setVisibility(View.VISIBLE);
                PlaceOrder();
                Log.d(TAG,"Afterplaceorder onclick method");
            }
        });

        change_address_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"going To change address");
                Intent intent=new Intent(ProductDetails.this,AddressList.class);
                intent.putExtra("source","ProductDetails");
                intent.putExtra("selected_product",selectedProduct);
                startActivity(intent);
            }
        });

    }

    private void setValueInAddress(TextView address_userName, TextView address_pincode, TextView address_full_address, TextView deliver_to_text) {
        UserRegistrationDTO userRegistrationDTO=mySharedPreferences.getUserDetailsFromSharedPreference();
        if(userRegistrationDTO.getAddressDTOList()!=null)
        {
            AddressDTO addressDTO=addressServices.getPrimaryAddress(userRegistrationDTO.getAddressDTOList());
            if(addressDTO!=null)
            {
                home.limitCharacterInView(address_userName,8,addressDTO.getName());

                address_pincode.setText(String.valueOf(addressDTO.getPincode()));
                home.limitCharacterInView(address_full_address,30,addressDTO.getArea_colony()+","+addressDTO.getHouse_no()+","+addressDTO.getCity());


            }


        }
        else {
            Log.d(TAG, "setValueInAddress: userRegistrationDTO.getAddressDTOList() is null");


        }


        

    }

    private void PlaceOrder() {

        OrderDetailsServices orderDetailsServices =new OrderDetailsServices(ProductDetails.this);

        orderDetailsServices.setListener(this);
        orderDetailsServices.placeOrder(selectedProduct,mySharedPreferences.getUserDetailsFromSharedPreference(),firebaseDatabase);
        Log.d(TAG,"below place order Main");


    }


    @Override
    public void onOrderSubmissionsuccess() {
        Log.d(TAG,"onSubmission Success Called");
        place_order_progressbar.setVisibility(View.GONE);

    }

    @Override
    public void onOrderSubmissionFailure() {
        Log.d(TAG,"onSubmission Failure Called");
        place_order_progressbar.setVisibility(View.GONE);
    }
}
