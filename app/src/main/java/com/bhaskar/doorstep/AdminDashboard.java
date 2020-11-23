package com.bhaskar.doorstep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bhaskar.doorstep.services.Home;

public class AdminDashboard extends AppCompatActivity {

    Button admin_add_product_btn,admin_slider_btn,admin_discount_btn,admin_recently_btn,admin_order_status_btn,admin_order_progresss_btn;
    ImageView admin_back_btn;
    Home home;
    TextView header_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        admin_add_product_btn=findViewById(R.id.admin_add_product_btn);
        admin_slider_btn=findViewById(R.id.admin_slider_btn);
        admin_discount_btn=findViewById(R.id.admin_discount_btn);
        admin_back_btn=findViewById(R.id.header_back_btn_image);
        header_title=findViewById(R.id.header_title);
        header_title.setText("Admin Dashboard");
        admin_recently_btn=findViewById(R.id.admin_recently_btn);
        admin_order_status_btn=findViewById(R.id.admin_order_status_btn);
        admin_order_progresss_btn=findViewById(R.id.admin_order_progresss_btn);
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
        admin_discount_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboard.this,AddDiscountProduct.class));
            }
        });
        admin_recently_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboard.this,AddRecentlyViewedProduct.class));
            }
        });
        admin_order_status_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboard.this,YourOrder.class));
            }
        });

        admin_order_progresss_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboard.this,OrderDashboard.class));
            }
        });

    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
        super.onBackPressed();
    }
}