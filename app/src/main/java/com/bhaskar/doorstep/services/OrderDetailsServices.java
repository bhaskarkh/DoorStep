package com.bhaskar.doorstep.services;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.bhaskar.doorstep.OrderSuccessOrFail;
import com.bhaskar.doorstep.R;
import com.bhaskar.doorstep.allinterface.OnOrderSubmissionListener;
import com.bhaskar.doorstep.allinterface.OrderStatusInterface;
import com.bhaskar.doorstep.model.AddressDTO;
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
import java.util.Map;


public class OrderDetailsServices {
    Context context;
    final String TAG = "OrderDetailsServices";
    OnOrderSubmissionListener listener;
    OrderStatusInterface orderStatusInterface;
    MySharedPreferences mySharedPreferences;
    AddressServices addressServices;


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
        orderDTO.setOrderStatus("Processing"); //order status >>pending >>confirmed >>completed >>failed
        orderDTO.setOrderDateTime(orderDateTime);
        orderDTO.setExpectedStartDateOfDelivery("ND"); //ND for not decided ,if order confirmed then agent will provide date and time
        orderDTO.setExpectedLastDateOfDelivery("ND");//same as above
        orderDTO.setOrderConfirmDate("ND");
        orderDTO.setCompleteDateTime("NC"); ////NC for not completed ,if order completed then agent will provide date and time
        orderDTO.setShippingDTO(generateShippingInfo(userDetailsFromSharedPreference, selectedProduct, orderId));
        Log.d(TAG, "userDetailsFromSharedPreference= " + userDetailsFromSharedPreference.toString());
        //Store Only Primary address in order
        addressServices=new AddressServices(context);
        AddressDTO addressDTO=addressServices.getPrimaryAddress(userDetailsFromSharedPreference.getAddressDTOList());
        List<AddressDTO> addressDTOList=new ArrayList<>();
        addressDTOList.add(addressDTO);
        userDetailsFromSharedPreference.setAddressDTOList(addressDTOList);

        orderDTO.setCustomerInfoDTO(userDetailsFromSharedPreference);
        orderDTO.setProductDTO(selectedProduct);
        orderDTO.setOrderConfirmed(false);
        orderDTO.setOrderCanceled(false);
        orderDTO.setOrderCancelDate("NA");

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
       /* String currentDateTime = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("DDMMYYYY Hmm");
        Date date = new Date();
        currentDateTime = simpleDateFormat.format(date);

        Log.d(TAG, "generated CurrentDateTime =" + currentDateTime);
        return currentDateTime;*/
        Home home=new Home(context);
       return home.getDefaultDateAndTimeInStringFormatFromDate(new Date());

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

    public  void getOrderListByCategory(String category,FirebaseDatabase firebaseDatabase,String source,boolean autoUpdate)
    {
        if (!autoUpdate) {
            setMySharedPreferences(context);
            Log.d(TAG, "getOrderListByCategory: ");
            if (!mySharedPreferences.checkSharedPrefExistsOrNot("orderListSharedPref", "orderListPref")) {
                Log.d(TAG, "getOrderListByCategory: shared Pref Doesn't Exist");
                getOrderListFromFirebase(firebaseDatabase, source,category);

            } else {
                Log.d(TAG, "getOrderListByCategory: shared Pref Exit");
                List<OrderDTO> orderDTOList = mySharedPreferences.getAllOrderListFromSharedPreference();
                setOrderListByCategoryType(category,orderDTOList);
            }
        }
        else {
            getOrderListFromFirebase(firebaseDatabase, source,category);
        }
    }
    public void refreshOrderByCategory(FirebaseDatabase firebaseDatabase,String category,String source) {
        getOrderListFromFirebase(firebaseDatabase,source,category);
    }

    public  void getOrderListFromFirebase(FirebaseDatabase firebaseDatabase,String source,String category)
    {
        //If source is SharedPref than it will store to shared pref otherwise data will be  directly from firebase real time
        //which means auto update is on
        mySharedPreferences=new MySharedPreferences(context);
        mySharedPreferences.setOrderStatusInterface(orderStatusInterface);
        final List<OrderDTO> orderDTOList=new ArrayList<>();
        DatabaseReference ref = firebaseDatabase.getReference().child("test").child("order");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderDTOList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Log.d(TAG, "onDataChange: ");
                    OrderDTO orderDTO=dataSnapshot.getValue(OrderDTO.class);
                    orderDTOList.add(orderDTO);

                }


                if (source.equalsIgnoreCase("SharedPref")) {
                    mySharedPreferences.setOrderListInSharedPreferenceAndShowInAdapter(orderDTOList);
                }else {
                    setOrderListByCategoryType(category,orderDTOList);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void setOrderListByCategoryType(String category,List<OrderDTO> orderDTOList)
    {

        if (category.equalsIgnoreCase("Show All")) {
            orderStatusInterface.setOrderStatusAdaptor(orderDTOList);
        }else {
            List<OrderDTO> oList = new ArrayList<>();
            for (OrderDTO orderDTO : orderDTOList) {
                if (orderDTO.getOrderStatus().equalsIgnoreCase(category)) {
                    oList.add(orderDTO);
                }
            }
            orderStatusInterface.setOrderStatusAdaptor(oList);
        }
    }

    public void setOrderProgressDetails(OrderDTO orderDTO, Map<String, TextView> textViewMap, Map<String, ImageView> imageViewMap, Map<String, ImageView> cicleImageMap,Map<String, View> viewMap)
    {
        if (orderDTO.getOrderStatus().equalsIgnoreCase("pending"))
        {

            designForPending(orderDTO,textViewMap,imageViewMap,cicleImageMap,viewMap);



        }

    }

    private void designForPending(OrderDTO orderDTO, Map<String, TextView> textViewMap, Map<String, ImageView> imageViewMap, Map<String, ImageView> cicleImageMap, Map<String, View> viewMap) {

        List<String> greenCircleTextList=new ArrayList<>();
        greenCircleTextList.add("placed_circle");
        setCircleGreen(greenCircleTextList,cicleImageMap);
    }

    private void setCircleGreen(List<String> greenCircleTextList,Map<String, ImageView> cicleImageMap) {
        for (Map.Entry <String,ImageView> entry:cicleImageMap.entrySet())
        {

            if (greenCircleTextList.contains(entry.getKey()))
            {
                entry.getValue().setImageResource(R.drawable.oval_shape_green);
            }
            else {
                entry.getValue().setImageResource(R.drawable.oval_shape_gray);
            }

        }
    }
    public void setMySharedPreferences(Context context) {
        mySharedPreferences=new MySharedPreferences(context);
        mySharedPreferences.setOrderStatusInterface(orderStatusInterface);

    }
    public void  setOrderDashboardAutoUpdateOnOrOff(boolean auto_update,FirebaseDatabase firebaseDatabase,String category)
    {
        setMySharedPreferences(context);
        mySharedPreferences.removeOrderListSharedPref();
        if(auto_update) {
            getOrderListFromFirebase(firebaseDatabase, "AutoUpdateOn",category);
        }
        else {
            getOrderListByCategory(category,firebaseDatabase,"SharedPref",false);
        }


    }
    public void setStatusBtnAndBackground(TextView currentStatus, TextView updateTo, TextView cancelled, String statusFromOrder, ImageView shippingEditBtn, Button editDeliveryDate, EditText shippingName,EditText shippingMobile,TextView confirmDateText,TextView confirmDate,TextView cancelOrCompletedDatetext,TextView cancelOrCompletedDate,OrderDTO orderDTO)
    {
        Log.d(TAG, "setStatusBtnAndBackground: statusFromOrder="+statusFromOrder);
        if(statusFromOrder.equalsIgnoreCase("Pending")||statusFromOrder.equalsIgnoreCase("Processing"))
        {
            Log.d(TAG, "Processing: ");
            currentStatus.setText(statusFromOrder);
            currentStatus.setBackgroundColor(getColorFromResource(R.color.processing));
            updateTo.setBackgroundResource(R.drawable.current_status_box_confirmed_update_to_svg);
            updateTo.setText("Confirmed");
            confirmDateText.setVisibility(View.INVISIBLE);
           confirmDate.setVisibility(View.INVISIBLE);
           cancelOrCompletedDatetext.setVisibility(View.INVISIBLE);
           cancelOrCompletedDate.setVisibility(View.INVISIBLE);


        }
        if(statusFromOrder.equalsIgnoreCase("Confirmed"))
        {
            Log.d(TAG, "Confirmed: ");
            currentStatus.setText(statusFromOrder);
            currentStatus.setBackgroundColor(getColorFromResource(R.color.confirmed));
            updateTo.setBackgroundResource(R.drawable.current_status_box_completed_update_to_svg);
            updateTo.setText("Completed");

            shippingName.setEnabled(false);
            shippingName.setFocusable(false);
            shippingMobile.setEnabled(false);
            shippingMobile.setFocusable(false);
            shippingEditBtn.setVisibility(View.VISIBLE);
            editDeliveryDate.setText("Edit Delivery Date");
            confirmDateText.setVisibility(View.VISIBLE);
            confirmDate.setVisibility(View.VISIBLE);
            confirmDate.setText(orderDTO.getOrderConfirmDate());
            cancelOrCompletedDatetext.setVisibility(View.INVISIBLE);
            cancelOrCompletedDate.setVisibility(View.INVISIBLE);


        }
        if(statusFromOrder.equalsIgnoreCase("Cancelled"))
        {
            Log.d(TAG, "Cancelled: ");
            currentStatus.setText(statusFromOrder);
            /*currentStatus.setBackgroundResource(R.drawable.current_status_box_cancelled_svg);*/
            currentStatus.setBackgroundColor(getColorFromResource(R.color.cancelled));

            shippingName.setEnabled(false);
            shippingName.setFocusable(false);
            shippingMobile.setEnabled(false);
            shippingMobile.setFocusable(false);


            updateTo.setVisibility(View.INVISIBLE);
            cancelled.setVisibility(View.INVISIBLE);

            if(orderDTO.isOrderConfirmed()) {
                editDeliveryDate.setText("Expected Delivery Date");
                confirmDateText.setVisibility(View.VISIBLE);
                confirmDate.setVisibility(View.VISIBLE);
                confirmDate.setText(orderDTO.getOrderConfirmDate());
            }else {
                confirmDateText.setVisibility(View.INVISIBLE);
                confirmDate.setVisibility(View.INVISIBLE);
            }
            editDeliveryDate.setEnabled(false);

            cancelOrCompletedDatetext.setVisibility(View.VISIBLE);
            cancelOrCompletedDate.setVisibility(View.VISIBLE);
            cancelOrCompletedDatetext.setText("Cancelled Date");
            cancelOrCompletedDate.setText(orderDTO.getOrderCancelDate());


        }
        if(statusFromOrder.equalsIgnoreCase("Completed")||statusFromOrder.equalsIgnoreCase("Completed"))
        {
            Log.d(TAG, "Completed: ");
            currentStatus.setText(statusFromOrder);
            currentStatus.setBackgroundColor(getColorFromResource(R.color.completed));
            shippingName.setEnabled(false);
            shippingName.setFocusable(false);
            shippingMobile.setEnabled(false);
            shippingMobile.setFocusable(false);
            updateTo.setVisibility(View.INVISIBLE);
            cancelled.setVisibility(View.INVISIBLE);
            editDeliveryDate.setText("Expected Delivery Date");
            editDeliveryDate.setEnabled(false);

            confirmDateText.setVisibility(View.VISIBLE);
            confirmDate.setVisibility(View.VISIBLE);
            cancelOrCompletedDatetext.setVisibility(View.VISIBLE);
            cancelOrCompletedDate.setVisibility(View.VISIBLE);
            cancelOrCompletedDatetext.setText("Completed Date");
            cancelOrCompletedDate.setText(orderDTO.getCompleteDateTime());

        }



    }

    public  int getColorFromResource(int color)
    {
        return context.getResources().getColor(color);
    }

    public void updateOrderStatus(OrderDTO orderDTO,ShippingDTO shippingDTO,String btnClicked)
    {
        if(btnClicked.equalsIgnoreCase("Confirmed"))
        {
            Log.d(TAG, "updateOrderStatus: Confirmed clicked ");
            orderDTO.setOrderStatus("Confirmed");
            orderDTO.setShippingDTO(shippingDTO);
            orderDTO.setOrderConfirmed(true);
            orderDTO.setOrderConfirmDate(getCurrentDateAndTime());
            changeOrderStatusToFireBaseAndSharedpref(orderDTO);

        }

        if (btnClicked.equalsIgnoreCase("Completed"))
        {
            Log.d(TAG, "updateOrderStatus: Confirmed clicked ");
            orderDTO.setOrderStatus("Completed");
            orderDTO.setShippingDTO(shippingDTO);
            orderDTO.setCompleteDateTime(getCurrentDateAndTime());
            changeOrderStatusToFireBaseAndSharedpref(orderDTO);

            Log.d(TAG, "updateOrderStatus: Completed clicked ");

        }
    }

    public void changeOrderStatusToFireBaseAndSharedpref(OrderDTO orderDTO) {
        MySharedPreferences mySharedPreferences=new MySharedPreferences(context);
        mySharedPreferences.setOrderStatusInterface(orderStatusInterface);


            Log.d(TAG, "changeOrderStatusToFireBaseAndSharedpref: ");
            DatabaseReference fireDataBaseReference = FirebaseDatabase.getInstance().getReference();

            fireDataBaseReference.child("test").child("order").child(orderDTO.getOrderId()).setValue(orderDTO)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: order stats change  Updated in firebase");

                            mySharedPreferences.changeValueofOrder(orderDTO);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Failed To change status of order try again", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: Failed To change status of order try again msg= "+e.getMessage());
                }
            });


    }

    public  void cancelOrder(OrderDTO orderDTO)
    {
        Home home=new Home(context);
        orderDTO.setOrderStatus("Cancelled");
        orderDTO.setOrderCanceled(true);
        orderDTO.setOrderCancelDate(home.getDefaultDateAndTimeInStringFormatFromDate(new Date()));
        changeOrderStatusToFireBaseAndSharedpref(orderDTO);

    }

}



