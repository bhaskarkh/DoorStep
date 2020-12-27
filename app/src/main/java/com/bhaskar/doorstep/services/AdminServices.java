package com.bhaskar.doorstep.services;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bhaskar.doorstep.R;
import com.bhaskar.doorstep.allinterface.AdminInterface;

public class AdminServices {
    private static final String TAG = "AdminServices";
    Context context;
    AdminInterface adminInterface;

    public void setAdminInterface(AdminInterface adminInterface) {
        this.adminInterface = adminInterface;
    }

    public AdminServices(Context context) {
        this.context = context;
    }

    public void ShowAdminConfirmationDialog(String callingSource)
    {
        Log.d(TAG, "ShowAdminConfirmationDialog: ");

        Dialog epicDialog = new Dialog(context);
        epicDialog.setContentView(R.layout.updatedelet);
        Button btnDelet = epicDialog.findViewById(R.id.deletebtnid);
        EditText passvlaue = epicDialog.findViewById(R.id.passChangeauth);
        Button verifyBtn = epicDialog.findViewById(R.id.verifypassbtn);
        verifyBtn.setBackgroundColor(context.getResources().getColor(R.color.button_color));
        verifyBtn.setTextColor(context.getResources().getColor(R.color.button_text_color));
        ImageView okimg = epicDialog.findViewById(R.id.verifyok);
        ImageView failedimg = epicDialog.findViewById(R.id.verifyfailed);
        LinearLayout layout1 = epicDialog.findViewById(R.id.layoutofbtn);
        ImageView btnNo = epicDialog.findViewById(R.id.noIdOnExitid);
        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        epicDialog.show();
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Popup Closed");

                dismissDialog(epicDialog);

            }
        });
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass=passvlaue.getText().toString();
              if(checkPassInput(pass,passvlaue))
              {
                 checkAdminPassFromFirebase(pass,callingSource,epicDialog);

              }

            }
        });

    }

    private void dismissDialog(Dialog epicDialog) {
        epicDialog.dismiss();
    }

    private void checkAdminPassFromFirebase(String pass, String callingSource, Dialog epicDialog) {
        Log.d(TAG, "checkAdminPassFromFirebase: pass= "+pass);
        boolean verificationResult=true;

        if (verificationResult) {
            dismissDialog(epicDialog);
            adminInterface.afterAdminVerificationSuccess(callingSource);
        }
        else {
            ImageView failedimg = epicDialog.findViewById(R.id.verifyfailed);
            failedimg.setVisibility(View.VISIBLE);
        }



    }

    private boolean checkPassInput(String pass, EditText passvlaue) {
        Log.d(TAG, "checkPassInput: pass length=  "+pass.length());
        if(pass.isEmpty())
        {
            passvlaue.setError("pass required");
            passvlaue.requestFocus();
            return false;
        }
        return true;
    }
}
