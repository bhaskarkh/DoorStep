package com.bhaskar.doorstep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;

public class OrderStatusDetail extends AppCompatActivity {

    View place_to_confirmed_line;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status_detail);
        place_to_confirmed_line=findViewById(R.id.placed_to_confirmed_line);
        TranslateAnimation animation=new TranslateAnimation(0,0,place_to_confirmed_line.getHeight(),0);
        animation.setDuration(2000);
        animation.setFillAfter(true);
        place_to_confirmed_line.setAnimation(animation);
    }
}