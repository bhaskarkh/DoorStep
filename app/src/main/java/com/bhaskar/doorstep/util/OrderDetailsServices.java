package com.bhaskar.doorstep.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bhaskar.doorstep.OrderSuccessOrFail;
import com.bhaskar.doorstep.allinterface.OnOrderSubmissionListener;
import com.bhaskar.doorstep.allinterface.OrderStatusInterface;
import com.bhaskar.doorstep.model.OrderDTO;
import com.bhaskar.doorstep.model.ProductDTO;
import com.bhaskar.doorstep.model.ShippingDTO;
import com.bhaskar.doorstep.model.UserRegistrationDTO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class OrderDetailsServices {
    Context context;
    final String TAG = "OrderDetailsServices";
    OnOrderSubmissionListener listener;
    OrderStatusInterface orderStatusInterface;


    public OrderDetailsServices(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setListener(OnOrderSubmissionListener listener) {
        this.listener = listener;
    }

    public void setOrderStatusInterface(OrderStatusInterface orderStatusInterface) {
        this.orderStatusInterface = orderStatusInterface;
    }

    public void placeOrder(ProductDTO selectedProduct, UserRegistrationDTO userDetailsFromSharedPreference, FirebaseDatabase firebaseDatabase) {
        Log.d(TAG, "inside  start placeOrder OrderDetailsServices");
        OrderDTO orderDTO = new OrderDTO();
        String orderId = generateOrderId();
        String orderDateTime = getCurrentDateAndTime();
        orderDTO.setOrderId(orderId);
        orderDTO.setOrderStatus("pending"); //order status >>pending >>confirmed >>completed >>failed
        orderDTO.setOrderDateTime(orderDateTime);
        orderDTO.setExpectedStartDateOfDelivery("ND"); //ND for not decided ,if order confirmed then agent will provide date and time
        orderDTO.setExpectedLastDateOfDelivery("ND");//same as above
        orderDTO.setOrderConfirmDate("ND");
        orderDTO.setCompleteDateTime("NC"); ////NC for not completed ,if order completed then agent will provide date and time
        orderDTO.setShippingDTO(generateShippingInfo(userDetailsFromSharedPreference, selectedProduct, orderId));
        Log.d(TAG, "userDetailsFromSharedPreference= " + userDetailsFromSharedPreference.toString());
        orderDTO.setCustomerInfoDTO(userDetailsFromSharedPreference);
        orderDTO.setProductDTO(selectedProduct);

        firebaseDatabase.getReference().child("test").child("order").child(orderId).setValue(orderDTO).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Log.d(TAG, "order placed Successfully");
                listener.onOrderSubmissionsuccess();
                Toast.makeText(context, "order placed Successfully", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, OrderSuccessOrFail.class);
                i.putExtra("title", "Order success");
                context.startActivity(i);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onOrderSubmissionFailure();
                Log.d(TAG, "order Failed Try Agin exception msg= " + e.getMessage());
                Toast.makeText(context, "order Failed Try Agin", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, OrderSuccessOrFail.class);
                i.putExtra("title", "Order Failed");
                context.startActivity(i);
            }
        });

        Log.d(TAG, "inside  end placeOrder OrderDetailsServices");

    }

    private ShippingDTO generateShippingInfo(UserRegistrationDTO userDetailsFromSharedPreference, ProductDTO selectedProduct, String orderId) {
        ShippingDTO shippingDTO = new ShippingDTO();
        /*ShippingServices shippingServices=new ShippingServices(this.context);
        shippingDTO=shippingServices.genrateShipping(userDetailsFromSharedPreference,selectedProduct,orderId);*/
        shippingDTO.setShippingId(orderId);

        return shippingDTO;
    }

    public String generateOrderId() {
        String orderID = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyHmmssSSS");
        Date date = new Date();
        Log.d(TAG,"date= "+date);
        String dateTime = simpleDateFormat.format(date);
        orderID = "TX" + dateTime;
        Log.d(TAG, "generated Orderid =" + orderID);
        return orderID;
    }

    public String getCurrentDateAndTime() {
        String currentDateTime = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("DDMMYYYY Hmm");
        Date date = new Date();
        currentDateTime = simpleDateFormat.format(date);

        Log.d(TAG, "generated CurrentDateTime =" + currentDateTime);
        return currentDateTime;
    }

    public void getOrderListByUserId(final String fuid, FirebaseDatabase firebaseDatabase)
    {
        final List<OrderDTO> orderDTOList=new ArrayList<>();
        DatabaseReference ref = firebaseDatabase.getReference().child("test").child("order");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    OrderDTO orderDTO=dataSnapshot.getValue(OrderDTO.class);
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {
                        if(dataSnapshot1.getKey().equalsIgnoreCase("customerInfoDTO"))
                        {
                            UserRegistrationDTO userDTO=dataSnapshot1.getValue(UserRegistrationDTO.class);
                            if(userDTO.getUserId().equalsIgnoreCase(fuid))
                            {
                                orderDTOList.add(orderDTO);
                            }

                        }

                    }


                }
              orderStatusInterface.setOrderStatusAdaptor(orderDTOList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}



