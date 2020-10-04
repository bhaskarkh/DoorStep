package com.bhaskar.doorstep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bhaskar.doorstep.adapter.OrderHistoryAdapter;
import com.bhaskar.doorstep.adapter.SingleCategoryAdapter;
import com.bhaskar.doorstep.allinterface.OrderStatusInterface;
import com.bhaskar.doorstep.model.OrderDTO;
import com.bhaskar.doorstep.util.OrderDetailsServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class YourOrder extends AppCompatActivity  implements OrderStatusInterface {

    RecyclerView your_order_recylcerview;
    List<OrderDTO> orderDTOList;
    OrderHistoryAdapter orderHistoryAdapter;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    public String TAG="YourOrder";
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_order);
        your_order_recylcerview=findViewById(R.id.order_st_recylerview);
        progressBar=findViewById(R.id.your_order_status_progressbar);
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        progressBar.setVisibility(View.VISIBLE);
        getAllOrderOfUser();

    }

    private void getAllOrderOfUser() {
        String fuid=firebaseAuth.getCurrentUser().getUid();
        if(!fuid.isEmpty())
        {
            OrderDetailsServices orderDetailsServices=new OrderDetailsServices(this);
            orderDetailsServices.setOrderStatusInterface(this);
            orderDetailsServices.getOrderListByUserId(fuid,firebaseDatabase);

        }
        else {
            Log.d(TAG,"fuid is null user in unauthenticated");
            Toast.makeText(this, "Failed to load order try again later", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void setOrderStatusAdaptor(List<OrderDTO> orderDTOList) {
        progressBar.setVisibility(View.GONE);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        your_order_recylcerview.setLayoutManager(layoutManager);
        your_order_recylcerview.setItemAnimator(new DefaultItemAnimator());
        orderHistoryAdapter=new OrderHistoryAdapter(this,orderDTOList);
        your_order_recylcerview.setAdapter(orderHistoryAdapter);

    }
}