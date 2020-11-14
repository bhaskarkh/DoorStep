package com.bhaskar.doorstep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bhaskar.doorstep.model.OrderDTO;
import com.bhaskar.doorstep.model.ProductDTO;
import com.bhaskar.doorstep.services.Home;
import com.bhaskar.doorstep.services.OrderDetailsServices;
import com.codesgood.views.JustifiedTextView;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.Calendar;
import java.util.Date;

public class OrderStatusChange extends AppCompatActivity {

    private static final String TAG = "OrderStatusChange";
    Button select_start_end_date;
    MaterialDatePicker materialDatePicker;
    JustifiedTextView order_status_change_productName;
    Home home;
    OrderDTO orderDTO;
    OrderDetailsServices orderDetailsServices;
    TextView
            order_status_change_txn_id,
            order_status_change_prodPrice_new,
            order_status_change_start_date,
            order_status_change_end_date,
            order_status_change_current_status_text,
            order_status_cahage_cancelled_text,
            order_status_change_completed_or_confirmed_text;
    ImageView order_status_change_big_image;
    EditText order_status_change_shipping_boy_name,order_status_change_shipping_boy_mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status_change);
        select_start_end_date=findViewById(R.id.order_status_change_select_start_end_date);
        order_status_change_productName=findViewById(R.id.order_status_change_productName);
        order_status_change_txn_id=findViewById(R.id.order_status_change_txn_id);
        order_status_change_prodPrice_new=findViewById(R.id.order_status_change_prodPrice_new);
        order_status_change_start_date=findViewById(R.id.order_status_change_start_date);
        order_status_change_end_date=findViewById(R.id.order_status_change_end_date);
        order_status_change_current_status_text=findViewById(R.id.order_status_change_current_status_text);
        order_status_cahage_cancelled_text=findViewById(R.id.order_status_cahage_cancelled_text);
        order_status_change_completed_or_confirmed_text=findViewById(R.id.order_status_change_completed_or_confirmed_text);
        order_status_change_big_image=findViewById(R.id.order_status_change_big_image);
        order_status_change_shipping_boy_name=findViewById(R.id.order_status_change_shipping_boy_name);
        order_status_change_shipping_boy_mobile=findViewById(R.id.order_status_change_shipping_boy_mobile);

        home=new Home(this);
        orderDetailsServices=new OrderDetailsServices(this);
        getALlIntentValueAndInitialize();
        setDatePickerBuilder();

        select_start_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                materialDatePicker.show(getSupportFragmentManager(),"DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection1) {
                Pair<Long,Long> selection= (Pair<Long, Long>) selection1;
                Date firstDate=new Date(selection.first);
                Date endDate=new Date(selection.second);
                order_status_change_start_date.setText(home.getDefaultDateInStringFormatFromDate(firstDate));
                order_status_change_end_date.setText(home.getDefaultDateInStringFormatFromDate(endDate));
            }

        });

    }

    private void getALlIntentValueAndInitialize() {
        Intent intent=getIntent();
        orderDTO=intent.getParcelableExtra("OrderInfo");
        Log.d(TAG, "getALlIntentValue: orderDTO= "+orderDTO.toString());
        if(orderDTO!=null) {
            ProductDTO productDTO=orderDTO.getProductDTO();
            Log.d(TAG, "getALlIntentValueAndInitialize: "+productDTO.toString());
            home.loadImageInGlide(order_status_change_big_image,productDTO.getImage());
            order_status_change_productName.setText(productDTO.getName());
            order_status_change_txn_id.setText(orderDTO.getOrderId());
            order_status_change_prodPrice_new.setText(String.valueOf(productDTO.getPrice()));
            if(orderDTO.getExpectedStartDateOfDelivery()!=null)
                order_status_change_start_date.setText(orderDTO.getExpectedStartDateOfDelivery());
            if (orderDTO.getExpectedLastDateOfDelivery()!=null)
                order_status_change_end_date.setText(orderDTO.getExpectedLastDateOfDelivery());
            orderDetailsServices.setStatusBtnAndBackground(order_status_change_current_status_text,order_status_change_completed_or_confirmed_text,order_status_cahage_cancelled_text,orderDTO.getOrderStatus());



        }




    }


    private void setDatePickerBuilder() {
        Log.d(TAG, "setDatePickerBuilder: ");
        MaterialDatePicker.Builder<Pair<Long,Long>> builder=MaterialDatePicker.Builder.dateRangePicker();
        long today=MaterialDatePicker.todayInUtcMilliseconds();
        Calendar calendar=Calendar.getInstance();
        Log.d(TAG, "current date and Time in sdf=  "+home.getDefaultDateInStringFormatFromDate(calendar.getTime()));
        long currentMonth=calendar.getTimeInMillis();  //calendar start from current month to +1 month
        calendar.add(calendar.DATE,5);
        long lastDate=calendar.getTimeInMillis();
        calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)+1);
        long endMonth=calendar.getTimeInMillis();


        Log.d(TAG, "onCreate: currentMonth=  "+currentMonth+" endMonth= "+endMonth);
        CalendarConstraints.Builder constraintBuilder=new CalendarConstraints.Builder();
        constraintBuilder.setStart(currentMonth);
        constraintBuilder.setEnd(endMonth);
        constraintBuilder.setValidator(DateValidatorPointForward.now());

        Pair<Long,Long> DefaultPairDate=new Pair<>(today,lastDate);
        builder.setTitleText("Select a Date");
        builder.setSelection(DefaultPairDate);
        builder.setCalendarConstraints(constraintBuilder.build());
        materialDatePicker= builder.build();


    }
}