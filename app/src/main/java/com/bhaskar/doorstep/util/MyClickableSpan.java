package com.bhaskar.doorstep.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.bhaskar.doorstep.OrderSuccessOrFail;
import com.bhaskar.doorstep.R;
import com.bhaskar.doorstep.YourOrder;

public class MyClickableSpan extends ClickableSpan {
    Context context;
    private String TAG="MyClickableSpan";

    public MyClickableSpan(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(@NonNull View widget) {
        Log.d(TAG,"Your order clicked");
        context.startActivity(new Intent(context, YourOrder.class));

    }

    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        super.updateDrawState(ds);
        Log.d(TAG,"inside updateDrawstate");
        ds.setUnderlineText(true);
        ds.setColor(context.getResources().getColor(R.color.red));
    }
}
