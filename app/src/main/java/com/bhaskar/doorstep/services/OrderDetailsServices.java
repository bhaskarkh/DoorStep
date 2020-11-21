package com.bhaskar.doorstep.services;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Environment;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class OrderDetailsServices {
    Context context;
    final String TAG = "OrderDetailsServices";
    OnOrderSubmissionListener listener;
    OrderStatusInterface orderStatusInterface;
    MySharedPreferences mySharedPreferences;
    AddressServices addressServices;
    Home home;



    public OrderDetailsServices(Context context) {
        this.context = context;
        home=new Home(context);

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
                    if (category.equalsIgnoreCase("Show All")) {
                        mySharedPreferences.setOrderListInSharedPreferenceAndShowInAdapter(orderDTOList);
                    }else {
                        List<OrderDTO> oList = new ArrayList<>();
                        for (OrderDTO orderDTO : orderDTOList) {
                            if (orderDTO.getOrderStatus().equalsIgnoreCase(category)) {
                                oList.add(orderDTO);
                            }
                        }
                        mySharedPreferences.setOrderListInSharedPreferenceAndShowInAdapter(oList);
                    }

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
        if (orderDTO.getOrderStatus().equalsIgnoreCase("Processing"))
        {

            designForProcessing(orderDTO,textViewMap,imageViewMap,cicleImageMap,viewMap);



        }
        if(orderDTO.getOrderStatus().equalsIgnoreCase("Confirmed"))
        {
            designForConfirmed(orderDTO,textViewMap,imageViewMap,cicleImageMap,viewMap);
        }
        if(orderDTO.getOrderStatus().equalsIgnoreCase("Completed"))
        {
            designForCompleted(orderDTO,textViewMap,imageViewMap,cicleImageMap,viewMap);
        }
        if(orderDTO.getOrderStatus().equalsIgnoreCase("Cancelled"))
        {
            designForCancelled(orderDTO,textViewMap,imageViewMap,cicleImageMap,viewMap);
        }

    }

    private void designForProcessing(OrderDTO orderDTO, Map<String, TextView> textViewMap, Map<String, ImageView> imageViewMap, Map<String, ImageView> cicleImageMap, Map<String, View> viewMap) {

        setLineValue("Processing",orderDTO,viewMap);
        setCircleValue("Processing",orderDTO,cicleImageMap);
        setTextValue("Processing",orderDTO,textViewMap);
        setImageValue("Processing",orderDTO,imageViewMap);


    }



    private void designForConfirmed(OrderDTO orderDTO, Map<String, TextView> textViewMap, Map<String, ImageView> imageViewMap, Map<String, ImageView> cicleImageMap, Map<String, View> viewMap) {

        setLineValue("Confirmed",orderDTO,viewMap);
        setCircleValue("Confirmed",orderDTO,cicleImageMap);
        setTextValue("Confirmed",orderDTO,textViewMap);
        setImageValue("Confirmed",orderDTO,imageViewMap);


    }
    private void designForCompleted(OrderDTO orderDTO, Map<String, TextView> textViewMap, Map<String, ImageView> imageViewMap, Map<String, ImageView> cicleImageMap, Map<String, View> viewMap) {

        setLineValue("Completed",orderDTO,viewMap);
        setCircleValue("Completed",orderDTO,cicleImageMap);
        setTextValue("Completed",orderDTO,textViewMap);
        setImageValue("Completed",orderDTO,imageViewMap);


    }
    private void designForCancelled(OrderDTO orderDTO, Map<String, TextView> textViewMap, Map<String, ImageView> imageViewMap, Map<String, ImageView> cicleImageMap, Map<String, View> viewMap) {

        setLineValue("Cancelled",orderDTO,viewMap);
        setCircleValue("Cancelled",orderDTO,cicleImageMap);
        setTextValue("Cancelled",orderDTO,textViewMap);
        setImageValue("Cancelled",orderDTO,imageViewMap);


    }
    private void setLineValue(String orderStatus, OrderDTO orderDTO, Map<String, View> viewMap) {


        List<String> greenLineList=new ArrayList<>();

        if(orderStatus.equalsIgnoreCase("Confirmed"))
        {
            greenLineList.add("placed_to_confirmed_line");
        }
        if(orderStatus.equalsIgnoreCase("Completed"))
        {
            greenLineList.add("placed_to_confirmed_line");
            greenLineList.add("confirmed_to_completed_or_cancel");
        }
        if(orderStatus.equalsIgnoreCase("Cancelled"))
        {
            if (orderDTO.isOrderConfirmed())
            {
                greenLineList.add("placed_to_confirmed_line");
                greenLineList.add("confirmed_to_completed_or_cancel");

            }
            else {
                greenLineList.add("placed_to_confirmed_line");
            }

        }
        setLineGreen(greenLineList,viewMap,orderDTO);
    }

    private void setCircleValue(String orderStatus, OrderDTO orderDTO, Map<String, ImageView> cicleImageMap) {
        List<String> greenCircleTextList=new ArrayList<>();
        greenCircleTextList.add("placed_circle");
        if(orderStatus.equalsIgnoreCase("Confirmed"))
        {
            greenCircleTextList.add("confirmed_circle");
        }
        if(orderStatus.equalsIgnoreCase("Completed"))
        {
            greenCircleTextList.add("confirmed_circle");
            greenCircleTextList.add("completed_or_cancelled_circle");
        }
        if(orderStatus.equalsIgnoreCase("Cancelled"))
        {

            if (orderDTO.isOrderConfirmed())
            {
                greenCircleTextList.add("confirmed_circle");
                greenCircleTextList.add("completed_or_cancelled_circle");
            }
            else {
                greenCircleTextList.add("confirmed_circle");
            }
        }

        setCircleGreen(greenCircleTextList,cicleImageMap,orderDTO);
    }
    /* if(orderStatus.equalsIgnoreCase("Processing"))
    {}
        if(orderStatus.equalsIgnoreCase("Confirmed"))
    {}
        if(orderStatus.equalsIgnoreCase("Completed"))
    {}
        if(orderStatus.equalsIgnoreCase("Cancelled"))
    {}*/

    private void setImageValue(String orderStatus,OrderDTO orderDTO, Map<String, ImageView> imageViewMap) {
         for (Map.Entry<String, ImageView> entry : imageViewMap.entrySet()) {

                if(orderStatus.equalsIgnoreCase("Cancelled"))
                {
                    if(!orderDTO.isOrderConfirmed()) {
                        if (entry.getKey().equalsIgnoreCase("order_completed_or_cancelled_image"))
                            entry.getValue().setVisibility(View.INVISIBLE);
                        if (entry.getKey().equalsIgnoreCase("order_confirmed_image"))
                            entry.getValue().setImageResource(R.drawable.ic_order_cancelled);
                    }
                    else {
                        if (entry.getKey().equalsIgnoreCase("order_completed_or_cancelled_image"))
                            entry.getValue().setImageResource(R.drawable.ic_order_cancelled);
                    }


                }

        }



    }

    private void setTextValue(String orderStatus, OrderDTO orderDTO, Map<String, TextView> textViewMap) {
       // order_std_arrival_date
        if(orderStatus.equalsIgnoreCase("Processing"))
        {
            for(Map.Entry<String,TextView> entry:textViewMap.entrySet())
            {
                if (entry.getKey().equalsIgnoreCase("order_std_placed_txt3_date"))
                  entry.getValue().setText(home.getDateToShowInStringFromStringDayAndMonthOnly(orderDTO.getOrderDateTime()));
                if (entry.getKey().equalsIgnoreCase("order_std_confirmed_txt3_date"))
                    entry.getValue().setVisibility(View.INVISIBLE);
                if (entry.getKey().equalsIgnoreCase("order_std_completed_txt3_date"))
                    entry.getValue().setVisibility(View.INVISIBLE);
                if (entry.getKey().equalsIgnoreCase("order_std_arrival_date"))
                    entry.getValue().setText("We are Processing your request");



            }

        }
        if(orderStatus.equalsIgnoreCase("Confirmed"))
        {
            String expectedDate=getExpectedDateofDelivery(orderDTO.getExpectedStartDateOfDelivery(),orderDTO.getExpectedLastDateOfDelivery(),"date");

            for(Map.Entry<String,TextView> entry:textViewMap.entrySet())
            {

                if (entry.getKey().equalsIgnoreCase("order_std_placed_txt3_date"))
                    entry.getValue().setText(home.getDateToShowInStringFromStringDayAndMonthOnly(orderDTO.getOrderDateTime()));
                if (entry.getKey().equalsIgnoreCase("order_std_confirmed_txt3_date"))
                    entry.getValue().setText(home.getDateToShowInStringFromStringDayAndMonthOnly(orderDTO.getOrderConfirmDate()));
                if (entry.getKey().equalsIgnoreCase("order_std_completed_txt3_date")) {
                    entry.getValue().setText(expectedDate);
                }
                if (entry.getKey().equalsIgnoreCase("order_std_arrival_date"))
                {  entry.getValue().setText("Arriving on "+expectedDate);
                }



            }

        }
        if(orderStatus.equalsIgnoreCase("Completed"))
        {
            for(Map.Entry<String,TextView> entry:textViewMap.entrySet())
            {
                if (entry.getKey().equalsIgnoreCase("order_std_placed_txt3_date"))
                    entry.getValue().setText(home.getDateToShowInStringFromStringDayAndMonthOnly(orderDTO.getOrderDateTime()));
                if (entry.getKey().equalsIgnoreCase("order_std_confirmed_txt3_date"))
                    entry.getValue().setText(home.getDateToShowInStringFromStringDayAndMonthOnly(orderDTO.getOrderConfirmDate()));
                if (entry.getKey().equalsIgnoreCase("order_std_completed_txt3_date"))
                    entry.getValue().setText(home.getDateToShowInStringFromStringDayAndMonthOnly(orderDTO.getCompleteDateTime()));
                if (entry.getKey().equalsIgnoreCase("order_std_arrival_date"))
                   entry.getValue().setText("Order Completed on "+home.getDateToShowInStringFromStringDayAndMonthOnly(orderDTO.getCompleteDateTime()));

            }

        }
        if(orderStatus.equalsIgnoreCase("Cancelled"))
        {
          if(orderDTO.isOrderConfirmed()) {
              for (Map.Entry<String, TextView> entry : textViewMap.entrySet()) {
                  if (entry.getKey().equalsIgnoreCase("order_std_placed_txt3_date"))
                      entry.getValue().setText(home.getDateToShowInStringFromStringDayAndMonthOnly(orderDTO.getOrderDateTime()));
                  if (entry.getKey().equalsIgnoreCase("order_std_confirmed_txt3_date"))
                      entry.getValue().setText(home.getDateToShowInStringFromStringDayAndMonthOnly(orderDTO.getOrderConfirmDate()));
                  if (entry.getKey().equalsIgnoreCase("order_std_completed_txt3_date")) {
                      entry.getValue().setText(home.getDateToShowInStringFromStringDayAndMonthOnly(orderDTO.getOrderCancelDate()));
                  }
                  if (entry.getKey().equalsIgnoreCase("order_std_completed_txt1")) {
                      entry.getValue().setText("Order Cancelled");
                  }
                  if (entry.getKey().equalsIgnoreCase("order_std_completed_txt2")) {
                      entry.getValue().setText("Your Order is Cancelled");
                  }
                  if (entry.getKey().equalsIgnoreCase("order_std_arrival_date"))
                      entry.getValue().setText("Order Cancelled on "+home.getDateToShowInStringFromStringDayAndMonthOnly(orderDTO.getOrderCancelDate()));


              }
          }
          else {
                 for (Map.Entry<String, TextView> entry : textViewMap.entrySet()) {

                     if (entry.getKey().equalsIgnoreCase("order_std_placed_txt3_date"))
                         entry.getValue().setText(home.getDateToShowInStringFromStringDayAndMonthOnly(orderDTO.getOrderDateTime()));
                     if (entry.getKey().equalsIgnoreCase("order_std_confirmed_txt1"))
                         entry.getValue().setText("Order Cancelled");
                     if (entry.getKey().equalsIgnoreCase("order_std_confirmed_txt2"))
                         entry.getValue().setText("Your Order is Cancelled");
                     if (entry.getKey().equalsIgnoreCase("order_std_confirmed_txt3_date"))
                         entry.getValue().setText(home.getDateToShowInStringFromStringDayAndMonthOnly(orderDTO.getOrderCancelDate()));
                     if (entry.getKey().equalsIgnoreCase("order_std_completed_txt1"))
                         entry.getValue().setVisibility(View.INVISIBLE);
                     if (entry.getKey().equalsIgnoreCase("order_std_completed_txt2"))
                         entry.getValue().setVisibility(View.INVISIBLE);
                     if (entry.getKey().equalsIgnoreCase("order_std_completed_txt3_date"))
                         entry.getValue().setVisibility(View.INVISIBLE);
                     if (entry.getKey().equalsIgnoreCase("order_std_arrival_date"))
                         entry.getValue().setText("Order Cancelled on "+home.getDateToShowInStringFromStringDayAndMonthOnly(orderDTO.getOrderCancelDate()));


              }

          }

        }
    }

    private String getExpectedDateofDelivery(String expectedStartDateOfDelivery, String expectedLastDateOfDelivery,String source) {
        String expectedDate="";
        String firstDate=expectedStartDateOfDelivery.substring(0,2);

         expectedDate=firstDate+"-"+home.getDateToShowInStringFromStringDayAndMonthOnly(expectedLastDateOfDelivery);



        return expectedDate;
    }

    private void setCircleGreen(List<String> greenCircleTextList,Map<String, ImageView> cicleImageMap,OrderDTO orderDTO) {

        for (Map.Entry <String,ImageView> entry:cicleImageMap.entrySet())
        {

            if (orderDTO.getOrderStatus().equalsIgnoreCase("Cancelled")&& !orderDTO.isOrderConfirmed())
            {
                if(entry.getKey().equalsIgnoreCase("completed_or_cancelled_circle"))
                    entry.getValue().setVisibility(View.INVISIBLE);
                if(entry.getKey().equalsIgnoreCase("confirmed_circle"))
                    entry.getValue().setImageResource(R.drawable.oval_shape_green);


            }
            else {
                if (greenCircleTextList.contains(entry.getKey())) {

                    entry.getValue().setImageResource(R.drawable.oval_shape_green);
                } else {
                    entry.getValue().setImageResource(R.drawable.oval_shape_gray);
                }
            }

        }
    }
    private void setLineGreen(List<String> greenLineList,Map<String, View> viewMap,OrderDTO orderDTO) {

        for (Map.Entry <String,View> entry:viewMap.entrySet())
        {
            if (orderDTO.getOrderStatus().equalsIgnoreCase("Cancelled")&& !orderDTO.isOrderConfirmed())
            {
                if(entry.getKey().equalsIgnoreCase("confirmed_to_completed_or_cancel"))
                    entry.getValue().setVisibility(View.INVISIBLE);
                if(entry.getKey().equalsIgnoreCase("placed_to_confirmed_line"))
                    entry.getValue().setBackgroundResource(R.color.green);
            }

            else {
                if (greenLineList.contains(entry.getKey())) {
                    entry.getValue().setBackgroundResource(R.color.green);
                } else {
                    entry.getValue().setBackgroundResource(R.color.gray);
                }
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

        orderDTO.setOrderStatus("Cancelled");
        orderDTO.setOrderCanceled(true);
        orderDTO.setOrderCancelDate(home.getDefaultDateAndTimeInStringFormatFromDate(new Date()));
        changeOrderStatusToFireBaseAndSharedpref(orderDTO);

    }
    
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void generatePdfFromOrderValue(OrderDTO orderDTO) {
        int width = 1200;
        int height = 2000;
        Bitmap gridbmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.gridimage);
        Bitmap gridscaledbmp = Bitmap.createScaledBitmap(gridbmp, width-1, height-1, false);
        Bitmap headerImage=BitmapFactory.decodeResource(context.getResources(), R.drawable.invoice_heade_imager);
        Bitmap hedaerscaledbmp = Bitmap.createScaledBitmap(headerImage, width-50, 621, false);

        Log.d(TAG, "generatePdfFromOrderValue: ");
        PdfDocument pdfDocument = new PdfDocument();
        Paint myPaint = new Paint();
        Paint titlePaint = new Paint();
        PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(width, height, 1).create();
        PdfDocument.Page myPage1 = pdfDocument.startPage(myPageInfo1);
        Canvas canvas = myPage1.getCanvas();
        canvas.drawBitmap(gridscaledbmp, 0, 0, myPaint);
        canvas.drawBitmap(hedaerscaledbmp,50,50,myPaint);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTextSize(70);
        canvas.drawText("DoorStep", width / 2, 300, titlePaint);
        pdfDocument.finishPage(myPage1);

        String extr = Environment.getExternalStorageDirectory().toString();
        File fileDir = new File(extr + "/DoorStep/invoice");

        if (!fileDir.exists()) {
            Log.d(TAG, "generatePdfFromOrderValue: fileDir not exist");
            fileDir.mkdirs();
            Log.d(TAG, "generatePdfFromOrderValue: after mkdir");
            CreateFile(fileDir,pdfDocument);
        } else {
            Log.d(TAG, "generatePdfFromOrderValue: path exist");
            CreateFile(fileDir,pdfDocument);
        }

       


    }
    
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void CreateFile(File fileDir, PdfDocument pdfDocument){
        Log.d(TAG, "CreateFile: ");
        File pdfFile = new File(fileDir.getAbsolutePath(), "bhaskardoorstep.pdf");

        try {
            Log.d(TAG, "generatePdfFromOrderValue: ");
            pdfDocument.writeTo(new FileOutputStream(pdfFile));
            Toast.makeText(context, "File Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.d(TAG, "generatePdfFromOrderValue: IOException " + e.getMessage());
        } finally {
            Log.d(TAG, "CreateFile: finnaly close");
            pdfDocument.close();
        }

    }
        

}



