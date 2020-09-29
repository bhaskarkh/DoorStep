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

import com.bhaskar.doorstep.model.ProductDTO;
import com.bhaskar.doorstep.util.MySharedPreferences;
import com.bhaskar.doorstep.util.OrderDetailsServices;
import com.google.firebase.database.FirebaseDatabase;

public class ProductDetails extends AppCompatActivity {

    ImageView img, back;
    TextView proName, proPrice, proDesc, proQty, proUnit;

    String name, price, desc, qty, unit;

    int image;
    Button buy_btn;
    ProductDTO selectedProduct;
    FirebaseDatabase firebaseDatabase;
    ProgressBar place_order_progressbar;
    final String TAG="ProductDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        proName = findViewById(R.id.productName);
        proDesc = findViewById(R.id.prodDesc);
        proPrice = findViewById(R.id.prodPrice);
        img = findViewById(R.id.big_image);
        back = findViewById(R.id.back2);
        proQty = findViewById(R.id.qty);
        proUnit = findViewById(R.id.unit);
        buy_btn=findViewById(R.id.buy_btn);
        place_order_progressbar=findViewById(R.id.place_order_progressbar);
        firebaseDatabase=FirebaseDatabase.getInstance();

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
         qty = String.valueOf(selectedProduct.getQuantity());
         unit = selectedProduct.getQuantityType();


         proName.setText(name);
         proPrice.setText(price);
         proDesc.setText(desc);
         proQty.setText(qty);
         proUnit.setText(unit);


        img.setImageResource(R.drawable.b1);


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

    }

    private void PlaceOrder() {

        OrderDetailsServices orderDetailsServices =new OrderDetailsServices(ProductDetails.this);
        MySharedPreferences mySharedPreferences=new MySharedPreferences(ProductDetails.this);
        orderDetailsServices.placeOrder(selectedProduct,mySharedPreferences.getUserDetailsFromSharedPreference(),firebaseDatabase);
        Log.d(TAG,"below place order Main");


    }


}
