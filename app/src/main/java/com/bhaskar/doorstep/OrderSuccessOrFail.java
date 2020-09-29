package com.bhaskar.doorstep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderSuccessOrFail extends AppCompatActivity {
    TextView order_title;
    ImageView order_success_back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success_or_fail);
        order_title=findViewById(R.id.order_success_fail_title);
        String title=getIntent().getStringExtra("title");
        order_title.setText(title);
        order_success_back_btn=findViewById(R.id.order_success_back_btn);

        order_success_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrderSuccessOrFail.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }
}