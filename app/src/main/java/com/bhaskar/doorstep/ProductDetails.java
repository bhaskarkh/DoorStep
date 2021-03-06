package com.bhaskar.doorstep;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bhaskar.doorstep.model.AddressDTO;
import com.bhaskar.doorstep.model.ProductDTO;
import com.bhaskar.doorstep.model.UserRegistrationDTO;
import com.bhaskar.doorstep.services.AddressServices;
import com.bhaskar.doorstep.services.Home;
import com.bhaskar.doorstep.services.MySharedPreferences;
import com.bhaskar.doorstep.allinterface.OnOrderSubmissionListener;
import com.bhaskar.doorstep.services.OrderDetailsServices;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ProductDetails extends AppCompatActivity implements OnOrderSubmissionListener {

    ImageView img, back;
    TextView proName, proPrice, proDesc, proQty, proUnit;
    TextView address_userName,address_pincode,address_full_address,deliver_to_text,header_tittle;
    MySharedPreferences mySharedPreferences;
    AddressServices addressServices;
    Home home;
    String sourceCategory,sourceComingFrom;

    String name, price, desc, qty, unit;

    int image;
    Intent i;
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
        back = findViewById(R.id.header_back_btn_image);
        change_address_button=findViewById(R.id.change_address_button);
        address_userName=findViewById(R.id.prod_address_user_name);
        address_pincode=findViewById(R.id.prod_address_pincode);
        address_full_address=findViewById(R.id.prod_address_full_address);
        deliver_to_text=findViewById(R.id.prod_deliver_to_text);
        buy_btn=findViewById(R.id.buy_btn);
        header_tittle=findViewById(R.id.header_title);
        place_order_progressbar=findViewById(R.id.place_order_progressbar);
        header_tittle.setText("Product Details");
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        mySharedPreferences=new MySharedPreferences(ProductDetails.this);
        addressServices=new AddressServices(ProductDetails.this);
        home=new Home(ProductDetails.this);
          i = getIntent();
          //all Intent Value
        selectedProduct=i.getParcelableExtra("selected_product");
        sourceCategory=i.getStringExtra("cat_name");
        sourceComingFrom=i.getStringExtra("source_to_product_details");

         name = selectedProduct.getName();
         price = String.valueOf(selectedProduct.getPrice());
         desc = selectedProduct.getDescription();
         proName.setText(name);
         proPrice.setText(price);
         proDesc.setText(desc);
        Glide.with(ProductDetails.this).load(selectedProduct.getImage()).into(img);




        setValueInAddress(address_userName,address_pincode,address_full_address,deliver_to_text,change_address_button);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: BackButton productDetails");

              home.backButton(i,"ProductDetails");

            }
        });

        buy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                place_order_progressbar.setVisibility(View.VISIBLE);
                if(isValidOrder()) {
                    PlaceOrder();
                }else {
                    place_order_progressbar.setVisibility(View.GONE);
                }

                Log.d(TAG,"Afterplaceorder onclick method");
            }
        });

        change_address_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"going To change address");
                Intent intent=new Intent(ProductDetails.this,AddressList.class);
                intent.putExtra("source","ProductDetails");
                intent.putExtra("cat_name",sourceCategory);
                intent.putExtra("selected_product",selectedProduct);
                intent.putExtra("source_to_product_details",sourceComingFrom);

                startActivity(intent);
            }
        });

    }


    private boolean isValidOrder() {
        UserRegistrationDTO userRegistrationDTO=mySharedPreferences.getUserDetailsFromSharedPreference();


        if(userRegistrationDTO.getAddressDTOList()==null) {
            address_full_address.setVisibility(View.VISIBLE);
            address_full_address.setTextColor(getResources().getColor(R.color.red));
            address_full_address.setText("Kindly add Address to Place the order");

            Toast.makeText(this, "Kindly add Addres before Placing the order", Toast.LENGTH_SHORT).show();
            return false;
        }




        return true;
    }

    private void setValueInAddress(TextView address_userName, TextView address_pincode, TextView address_full_address, TextView deliver_to_text, Button change_address_button) {
        UserRegistrationDTO userRegistrationDTO=mySharedPreferences.getUserDetailsFromSharedPreference();
        if(userRegistrationDTO.getAddressDTOList()!=null)
        {
            AddressDTO addressDTO=addressServices.getPrimaryAddress(userRegistrationDTO.getAddressDTOList());
            if(addressDTO!=null)
            {
                home.limitCharacterInView(address_userName,8,addressDTO.getName());
                String pincodeWithComaPrefix=","+addressDTO.getPincode();

                address_pincode.setText(pincodeWithComaPrefix);
                home.limitCharacterInView(address_full_address,30,addressDTO.getArea_colony()+","+addressDTO.getHouse_no()+","+addressDTO.getCity());


            }


        }
        else {
            deliver_to_text.setText("No Address Added");
            address_userName.setVisibility(View.INVISIBLE);
            address_pincode.setVisibility(View.INVISIBLE);
            address_full_address.setVisibility(View.INVISIBLE);
            change_address_button.setText("Add");
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
