package com.bhaskar.doorstep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bhaskar.doorstep.util.Home;
import com.bhaskar.doorstep.util.MyClickableSpan;
import com.codesgood.views.JustifiedTextView;

public class OrderSuccessOrFail extends AppCompatActivity {
    TextView order_title;
    ImageView order_success_back_btn;
    Button order_success_home_btn;
    Home hm;
    TextView justifiedTextView;
    private String TAG="OrderSuccessOrFail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success_or_fail);
        String title=getIntent().getStringExtra("title");
        String thankyou_msg="we are currently processing your order.You can find updated to your order under Your Order";
                //this.getResources().getString(R.string.thank_you_for_your_order);

        order_success_back_btn=findViewById(R.id.order_success_back_btn);
        order_success_home_btn=findViewById(R.id.order_success_home_btn);
        justifiedTextView=findViewById(R.id.tv_justified_paragraph);
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
        setClickableText(thankyou_msg);

    }

    private void setClickableText(String thankyou_msg) {
        Log.d(TAG,"inside setClickableText");
        Log.d(TAG,"thankyou_msg.length()= "+thankyou_msg.length());
        SpannableString ss = new SpannableString(thankyou_msg);
        MyClickableSpan myClickableSpan=new MyClickableSpan(this);

        ss.setSpan(myClickableSpan, thankyou_msg.length()-10, thankyou_msg.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        Log.d(TAG,"ss= "+ss);
        justifiedTextView.setText(ss);
        justifiedTextView.setMovementMethod(LinkMovementMethod.getInstance());
        justifiedTextView.setHighlightColor(Color.TRANSPARENT);

    }


}