package com.bhaskar.doorstep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bhaskar.doorstep.util.Home;

public class AdminDashboard extends AppCompatActivity {

    Button admin_add_product_btn,admin_slider_btn,admin_discount_btn;
    ImageView admin_back_btn;
    Home home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        admin_add_product_btn=findViewById(R.id.admin_add_product_btn);
        admin_slider_btn=findViewById(R.id.admin_slider_btn);
        admin_discount_btn=findViewById(R.id.admin_discount_btn);
        admin_back_btn=findViewById(R.id.admin_back_btn);
        home=new Home(this);

        admin_add_product_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboard.this,AddProductInDb.class));
            }
        });
        admin_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.gotToHome();
            }
        });

    }
}