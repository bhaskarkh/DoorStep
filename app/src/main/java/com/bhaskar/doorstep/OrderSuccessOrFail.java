package com.bhaskar.doorstep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bhaskar.doorstep.util.Home;

public class OrderSuccessOrFail extends AppCompatActivity {
    TextView order_title;
    ImageView order_success_back_btn;
    Button order_success_home_btn;
    Home hm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success_or_fail);
        String title=getIntent().getStringExtra("title");

        order_success_back_btn=findViewById(R.id.order_success_back_btn);
        order_success_home_btn=findViewById(R.id.order_success_home_btn);
        hm=new Home(OrderSuccessOrFail.this);

        order_success_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hm.gotToHome();
            }
        });
        order_success_home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hm.gotToHome();
            }
        });

    }
}