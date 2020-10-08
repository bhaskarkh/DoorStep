package com.bhaskar.doorstep.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.bhaskar.doorstep.MainActivity;
import com.bhaskar.doorstep.R;

public class Home {
    Context context;
    private static final String TAG = "Home";

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

    public String getFirstUpperCase(String str)
    {
        String[] strArray = str.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : strArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            builder.append(cap).append(" ");
        }
        return builder.toString();
    }
    public void changeColorOfTextView(TextView order_status,String value,String source) {
        if (source.equals("order_status"))
        {
            orderStatusTextColor(order_status,value);
        }
    }

    public void orderStatusTextColor(TextView order_status,String value) {
        //Processing >> BLUE
        //Confirmed >> Green
        //Cancelled >> RED
        //Completed >> GRAY
        Log.d(TAG,"order status Value "+value);
        if (value.equals("Processing"))
        {
            order_status.setTextColor(context.getResources().getColor(R.color.blue));
        }
        else  if (value.equals("Confirmed"))
        {
            order_status.setTextColor(context.getResources().getColor(R.color.green));

        }
        else  if (value.equals("Cancelled"))
        {
            order_status.setTextColor(context.getResources().getColor(R.color.red));
        }
        else  if (value.equals("Completed"))
        {
            order_status.setTextColor(context.getResources().getColor(R.color.gray));
        }
        else {

            order_status.setTextColor(context.getResources().getColor(R.color.blue));
        }

    }

    public void limitCharacterInView(TextView textView,int max_char,String value)
    {
        String stringToShow=value.substring(0,max_char)+"...";
        textView.setText(stringToShow);

    }
    public String appendRuppeSymbol(String anount)
    {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(context.getResources().getString(R.string.Rs));
        stringBuilder.append(anount);
        return  stringBuilder.toString();
    }

}
