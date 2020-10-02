package com.bhaskar.doorstep.util;

import android.content.Context;
import android.content.Intent;

import com.bhaskar.doorstep.MainActivity;

public class Home {
    Context context;

    public Home(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void  gotToHome()
    {
        Intent intent=new Intent(context, MainActivity.class);
        context.startActivity(intent);

    }
}
