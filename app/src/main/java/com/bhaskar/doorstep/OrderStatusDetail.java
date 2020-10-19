package com.bhaskar.doorstep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.bhaskar.doorstep.util.Home;

public class OrderStatusDetail extends AppCompatActivity {

    View place_to_confirmed_line;
    ImageView order_std_back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status_detail);
        place_to_confirmed_line=findViewById(R.id.placed_to_confirmed_line);
        order_std_back_btn=findViewById(R.id.order_std_back_btn);
        TranslateAnimation animation=new TranslateAnimation(0,0,place_to_confirmed_line.getHeight(),0);
        animation.setDuration(2000);
        animation.setFillAfter(true);
        place_to_confirmed_line.setAnimation(animation);
        order_std_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderStatusDetail.this,YourOrder.class));
            }
        });
    }
}