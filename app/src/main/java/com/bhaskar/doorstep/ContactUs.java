package com.bhaskar.doorstep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bhaskar.doorstep.services.Home;

public class ContactUs extends AppCompatActivity {

    ImageView header_back_btn;
    TextView header_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        header_back_btn=findViewById(R.id.header_back_btn_image);
        header_title=findViewById(R.id.header_title);
        header_title.setText("Contact Us");
        header_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ContactUs.this, MainActivity.class));
            }
        });
    }
}