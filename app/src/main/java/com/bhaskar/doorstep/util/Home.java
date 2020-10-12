package com.bhaskar.doorstep.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.bhaskar.doorstep.AddressList;
import com.bhaskar.doorstep.MainActivity;
import com.bhaskar.doorstep.ProductDetails;
import com.bhaskar.doorstep.R;
import com.bhaskar.doorstep.SingleCategory;
import com.bhaskar.doorstep.model.ProductDTO;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

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



    public void backButton(Intent intent,String backCallSource)
    {
        Log.d(TAG, "backButton: Clicked inside Home");
        if (backCallSource.equals("ProductDetails"))
        {
            
            Log.d(TAG, "backButton: call from ProductDetails");
            String goingSource = intent.getStringExtra("cat_name");

            if (goingSource != null) {
                Log.d(TAG, "goingSource : "+goingSource);
                if (!goingSource.equals("NA")) {
                    Log.d(TAG, "backButton: not NA");

                    Intent i = new Intent(context, SingleCategory.class);
                    i.putExtra("cat_name", goingSource);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                    ((Activity)context).finish();
                } else {
                    Log.d(TAG, "goingSource: is NA ");
                    gotToHome();
                }
            }else {
                Log.d(TAG, "goingSource: is null");
                gotToHome();
            }

        }else if(backCallSource.equals("AddressList")){
            Log.d(TAG, "backButton: AddressList");
            String comingSource=intent.getStringExtra("source");


            if(comingSource!=null)
            {
                Log.d(TAG, "comingSource: "+comingSource);
                if (comingSource.equals("ProductDetails")){
                    Intent i = new Intent(context, ProductDetails.class);
                    ProductDTO productDTO=(ProductDTO)intent.getParcelableExtra("selected_product");
                    String cat_nam=intent.getStringExtra("cat_name");
                    Log.d(TAG, "AddressList: cat_name"+cat_nam);
                    i.putExtra("cat_name", cat_nam);
                    i.putExtra("selected_product",productDTO);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                    ((Activity)context).finish();
                }

            }else {
                Log.d(TAG, "AddressList : comingSource= is null");
                gotToHome();
            }



        }
        else if(backCallSource.equals("ChangeAddress")){
            Log.d(TAG, "backButton: ChangeAddress");
            String comingSource=intent.getStringExtra("source");

            if(comingSource!=null)
            {
                if (comingSource.equals("ProductDetails")){
                    Intent i = new Intent(context, AddressList.class);
                    i.putExtra("cat_name", intent.getStringExtra("cat_name"));
                    i.putExtra("source",intent.getStringExtra("source"));
                    i.putExtra("selected_product",(ProductDTO)intent.getParcelableExtra("selected_product"));
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                    ((Activity)context).finish();
                }

            }
            else {
                gotToHome();
            }



        }else {
            gotToHome();
        }
    }

    public void  gotToHome()
    {
        Log.d(TAG, "gotToHome: ");
        Intent intent=new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        ((Activity)context).finish();

    }
    public String getSource(Intent intent)
    {
        String src="No source found";
        if(intent!=null)
        {
           src=intent.getStringExtra("source");
           if(src==null)
           {
               src="No source found";
           }
        }
      return src;
    }
    public String getMethod(Intent intent)
    {
        String src="No method found";
        if(intent!=null)
        {
            src=intent.getStringExtra("method");
            if(src==null)
            {
                src="No method found";
            }
        }
        return src;
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
        if(value.length()>(max_char+3)) {
            String stringToShow = value.substring(0, max_char) + "...";
            textView.setText(stringToShow);
        }
        else {
            textView.setText(value);
        }

    }
    public String appendRuppeSymbol(String anount)
    {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(context.getResources().getString(R.string.Rs));
        stringBuilder.append(anount);
        return  stringBuilder.toString();
    }

    public boolean checkEmptyStringAndSetErrorMsgOfTextInputLayout(Map<TextInputLayout,String> map){
        AtomicBoolean EmptyString= new AtomicBoolean(true);
        for (Map.Entry<TextInputLayout, String> entry : map.entrySet()) {
            TextInputLayout key = entry.getKey();
            String value = entry.getValue();
            if (key.getEditText().getText().toString().isEmpty()) {
                Log.d(TAG, "checkEmptyStringAndSetErrorMsg: key= "+key.getEditText().getText().toString()+" value= "+value);
                EmptyString.set(false);
                key.setError(value);
                break;
            }

        }

        return EmptyString.get();
    }


    public boolean checkEmptyStringAndSetErrorMsgOfEditText(Map<EditText,String> map)
    {
        AtomicBoolean EmptyString= new AtomicBoolean(true);

        for (Map.Entry<EditText, String> entry : map.entrySet()) {
            EditText key = entry.getKey();
            String value = entry.getValue();
            if (key.getText().toString().isEmpty()) {
                Log.d(TAG, "checkEmptyStringAndSetErrorMsg: key= "+key.getText().toString()+" value= "+value);
                EmptyString.set(false);
                key.setError(value);
                break;
            }

        }

        return EmptyString.get();
    }


}
