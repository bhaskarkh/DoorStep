package com.bhaskar.doorstep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bhaskar.doorstep.adapter.AddDiscountRecentlyProductAdapter;
import com.bhaskar.doorstep.adapter.OrderDashBoardAdapter;
import com.bhaskar.doorstep.allinterface.OrderStatusInterface;
import com.bhaskar.doorstep.model.OrderDTO;
import com.bhaskar.doorstep.model.ProductDTO;
import com.bhaskar.doorstep.services.Home;
import com.bhaskar.doorstep.services.OrderDetailsServices;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OrderDashboard extends AppCompatActivity implements OrderStatusInterface {


    Home home;
    Spinner order_dashboard_cat;
    String category;
    OrderDetailsServices orderDetailsServices;
    FirebaseDatabase firebaseDatabase;
    RecyclerView order_dashboard_recyclerview;
    OrderDashBoardAdapter orderDashBoardAdapter;
    ProgressBar order_dashboard_progressbar;
    Button order_refresh_btn,auto_update_on_off_btn;
    boolean autoUpdate=false;
    private static final String TAG = "OrderDashboard";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_dashboard);

        order_dashboard_cat=findViewById(R.id.order_dashboard_cat);
        order_dashboard_recyclerview=findViewById(R.id.order_dashboard_recyclerview);
        order_dashboard_progressbar=findViewById(R.id.order_dashboard_progressbar);
        order_refresh_btn=findViewById(R.id.order_refresh_btn);
        auto_update_on_off_btn=findViewById(R.id.auto_update_on_off_btn);
        home=new Home(this);
        orderDetailsServices=new OrderDetailsServices(this);
        orderDetailsServices.setOrderStatusInterface(this);
        firebaseDatabase=FirebaseDatabase.getInstance();

        setAllSpinnerValue();
        order_refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!autoUpdate) {
                    order_dashboard_progressbar.setVisibility(View.VISIBLE);
                    orderDetailsServices.refreshOrderByCategory(firebaseDatabase, category,"SharedPref");
                }
                else {
                    order_dashboard_progressbar.setVisibility(View.VISIBLE);
                    orderDetailsServices.refreshOrderByCategory(firebaseDatabase, category,"AutoUpdateOn");
                   }
            }
        });
       // order_dashboard_cat.setAdapter();

        order_dashboard_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {

                if(v!=null) {
                    category = ((TextView) v.findViewById(R.id.spinner_name)).getText().toString();
                    Log.d(TAG, "onItemSelected: Scategory=" + category);
                    fetchOrderByCategory(category);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                Log.d(TAG, "onItemSelected: No Category Selected ");

            }

        });

        auto_update_on_off_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAutoUpdate();
            }
        });


    }

    private void setAutoUpdate() {
        autoUpdate=!autoUpdate;
        if (autoUpdate) {
            auto_update_on_off_btn.setText("Auto Update On");
        }
        else {
            auto_update_on_off_btn.setText("Auto Update Off");
        }

        orderDetailsServices.setOrderDashboardAutoUpdateOnOrOff(autoUpdate,firebaseDatabase,category);
    }

    private void fetchOrderByCategory(String category) {

        Log.d(TAG, "fetchOrderByCategory: category= "+category);
        Log.d(TAG, "fetchOrderByCategory: auto Update = "+autoUpdate);
        if(autoUpdate) {

            orderDetailsServices.getOrderListByCategory(category, firebaseDatabase, "AutoUpdateOn", autoUpdate);
        }
        else {
            orderDetailsServices.getOrderListByCategory(category, firebaseDatabase, "SharedPref", autoUpdate);
        }
    }

    private void setAllSpinnerValue() {

        String[] catList = OrderDashboard.this.getResources().getStringArray(R.array.order_category_array);
        String[] newCatArray=new String[catList.length+1];
        newCatArray[0]="Show All";
        for (int i=1;i<newCatArray.length;i++)
        {
            newCatArray[i]=catList[i-1];

        }
        home.setSpinnerAdapterForTextOnly(newCatArray,order_dashboard_cat,false);
    }

    private void setCategoryRecycler(List<OrderDTO> orderDTOList ) {
      //  productServices.sortProductList(productDTOList);
        order_dashboard_progressbar.setVisibility(View.GONE);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        order_dashboard_recyclerview.setLayoutManager(layoutManager);
        order_dashboard_recyclerview.setItemAnimator(new DefaultItemAnimator());
        orderDashBoardAdapter =new OrderDashBoardAdapter(this,orderDTOList);
        
        order_dashboard_recyclerview.setAdapter(orderDashBoardAdapter);

    }


    @Override
    public void setOrderStatusAdaptor(List<OrderDTO> orderDTOList) {
        Log.d(TAG, "setOrderStatusAdaptor:  called");
        if (orderDTOList.size()!=0)
         setCategoryRecycler(orderDTOList );
        else {
             setCategoryRecycler(orderDTOList );
            Toast.makeText(this, "No Order of That Category", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void orderStatusChange() {

    }
}