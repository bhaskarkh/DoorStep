package com.bhaskar.doorstep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bhaskar.doorstep.model.AddressDTO;
import com.bhaskar.doorstep.model.OrderDTO;
import com.bhaskar.doorstep.model.ProductDTO;
import com.bhaskar.doorstep.services.AddressServices;
import com.bhaskar.doorstep.services.Home;
import com.bhaskar.doorstep.services.OrderDetailsServices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderStatusDetail extends AppCompatActivity {

    View
            placed_to_confirmed_line,
            confirmed_to_delivering,
            delivering_to_complet_or_cancel
           ;

    ImageView order_std_back_btn,
            confirmed_circle,
            placed_circle,
            delivering_circle,
            completed_or_cancelled_circle,
            order_std_prodImage,
            order_placed_image,
            order_confirmed_image,
            order_delivering_image,
            order_completed_or_cancelled_image;

    TextView order_std_arrival_date,
            order_std_placed_txt1,
            order_std_placed_txt2,
            order_std_placed_txt3_date,
            order_std_confirmed_txt1,
            order_std_confirmed_txt2,
            order_std_confirmed_txt3_date,
            order_std_transaction_id,
            order_std_delivering_txt1,
            order_std_delivering_txt2,
            order_std_delivering_txt3_date,
            order_std_completed_txt1,
            order_std_completed_txt2,
            order_std_completed_txt3_date,
            order_std_address_name,
            order_std_primary_address_full;
    OrderDTO orderDTOInfo;
    Home home;
    AddressServices addressServices;
    Map<String,TextView> textViewMap=new HashMap<>();
    Map<String,ImageView> imageViewMap=new HashMap<>();
    Map<String,View> viewMap=new HashMap<>();
    OrderDetailsServices orderDetailsServices;

    private static final String TAG = "OrderStatusDetail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status_detail);
        placed_to_confirmed_line=findViewById(R.id.placed_to_confirmed_line);
        order_std_back_btn=findViewById(R.id.order_std_back_btn);
        order_std_prodImage=findViewById(R.id.order_std_prodImage);
        order_std_arrival_date=findViewById(R.id.order_std_arrival_date);
        order_std_placed_txt1=findViewById(R.id.order_std_placed_txt1);
        order_std_placed_txt2=findViewById(R.id.order_std_placed_txt2);
        order_std_placed_txt3_date=findViewById(R.id.order_std_placed_txt3_date);
        order_std_confirmed_txt1=findViewById(R.id.order_std_confirmed_txt1);
        order_std_confirmed_txt2=findViewById(R.id.order_std_confirmed_txt2);
        order_std_confirmed_txt3_date=findViewById(R.id.order_std_confirmed_txt3_date);
        order_std_transaction_id=findViewById(R.id.order_std_transaction_id);
        order_std_delivering_txt1=findViewById(R.id.order_std_delivering_txt1);
        order_std_delivering_txt2=findViewById(R.id.order_std_delivering_txt2);
        order_std_delivering_txt3_date=findViewById(R.id.order_std_delivering_txt3_date);
        order_std_completed_txt1=findViewById(R.id.order_std_completed_txt1);
        order_std_completed_txt2=findViewById(R.id.order_std_completed_txt2);
        order_std_completed_txt3_date=findViewById(R.id.order_std_completed_txt3_date);
        order_std_address_name=findViewById(R.id.order_std_address_name);
        order_std_primary_address_full=findViewById(R.id.order_std_primary_address_full);

        placed_circle=findViewById(R.id.placed_circle);
                placed_to_confirmed_line=findViewById(R.id.placed_to_confirmed_line);
                confirmed_circle=findViewById(R.id.confirmed_circle);
                confirmed_to_delivering=findViewById(R.id.confirmed_to_delivering);
                delivering_circle=findViewById(R.id.delivering_circle);
                delivering_to_complet_or_cancel=findViewById(R.id.delivering_to_complet_or_cancel);
                completed_or_cancelled_circle=findViewById(R.id.completed_or_cancelled_circle);

        order_placed_image=findViewById(R.id.order_placed_image);
                order_confirmed_image=findViewById(R.id.order_confirmed_image);
                order_delivering_image=findViewById(R.id.order_delivering_image);
                order_completed_or_cancelled_image=findViewById(R.id.order_completed_or_cancelled_image);



        home=new Home(this);
        addressServices=new AddressServices(this);
        orderDetailsServices=new OrderDetailsServices(this);
        Intent intent=getIntent();
        getIntentValue(intent);
        TranslateAnimation animation=new TranslateAnimation(0,0,placed_to_confirmed_line.getHeight(),0);
        animation.setDuration(2000);
        animation.setFillAfter(true);
        placed_to_confirmed_line.setAnimation(animation);
        order_std_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderStatusDetail.this,YourOrder.class));
            }
        });
    }

    private void setValueInOrderStatusDetails() {

        Log.d(TAG, "setValueInOrderStatusDetails: ");
        if(orderDTOInfo!=null)
        {
           order_std_transaction_id.setText(orderDTOInfo.getOrderId());
            home.loadImageInGlide(order_std_prodImage,orderDTOInfo.getProductDTO().getImage());
            AddressDTO addressDTO=addressServices.getPrimaryAddress(orderDTOInfo.getCustomerInfoDTO().getAddressDTOList());
            order_std_address_name.setText(addressDTO.getName());
            String fullAddress=addressServices.getFullAddress(addressDTO);
            Log.d(TAG, "full address= "+fullAddress);
            order_std_primary_address_full.setText(fullAddress);
            setAllMapValue();
            orderDetailsServices.setOrderProgressDetails(orderDTOInfo,textViewMap,imageViewMap,viewMap);

            
        }
        else
        {
            Log.d(TAG, "setValueInOrderStatusDetails: productInfo is null");
        }

    }

    private void setAllMapValue() {
        setAllTextView();
        setAllImageView();
        setAllView();
        
    }

    private void setAllTextView() {
        textViewMap.put("order_std_arrival_date",order_std_arrival_date);
        textViewMap.put("order_std_placed_txt1",order_std_placed_txt1);
        textViewMap.put("order_std_placed_txt2",order_std_placed_txt2);
        textViewMap.put("order_std_placed_txt3_date",order_std_placed_txt3_date);
        textViewMap.put("order_std_confirmed_txt1",order_std_confirmed_txt1);
        textViewMap.put("order_std_confirmed_txt2",order_std_confirmed_txt2);
        textViewMap.put("order_std_confirmed_txt3_date",order_std_confirmed_txt3_date);
        textViewMap.put("order_std_transaction_id",order_std_transaction_id);
        textViewMap.put("order_std_delivering_txt1",order_std_delivering_txt1);
        textViewMap.put("order_std_delivering_txt2",order_std_delivering_txt2);
        textViewMap.put("order_std_delivering_txt3_date",order_std_delivering_txt3_date);
        textViewMap.put("order_std_completed_txt1",order_std_completed_txt1);
        textViewMap.put("order_std_completed_txt2",order_std_completed_txt2);
        textViewMap.put("order_std_completed_txt3_date",order_std_completed_txt3_date);
    }
    private void setAllImageView() {
        imageViewMap.put("order_placed_image",order_placed_image);
        imageViewMap.put("order_confirmed_image",order_confirmed_image);
        imageViewMap.put("order_delivering_image",order_delivering_image);
        imageViewMap.put("order_completed_or_cancelled_image",order_completed_or_cancelled_image);
        imageViewMap.put("placed_circle",placed_circle);
        imageViewMap.put("confirmed_circle",confirmed_circle);
        imageViewMap.put("delivering_circle",delivering_circle);
        imageViewMap.put("completed_or_cancelled_circle",completed_or_cancelled_circle);
    }


    private void setAllView() {

        viewMap.put("placed_to_confirmed_line",placed_to_confirmed_line);
       viewMap.put("confirmed_to_delivering",confirmed_to_delivering);
      viewMap.put("delivering_to_complet_or_cancel",delivering_to_complet_or_cancel);

    }

    private void getIntentValue(Intent intent) {
        Log.d(TAG, "getIntentValue: ");
        orderDTOInfo=intent.getParcelableExtra("OrderInfo");
        setValueInOrderStatusDetails();
    }
}