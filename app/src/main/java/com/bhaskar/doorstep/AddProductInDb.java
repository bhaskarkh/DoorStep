package com.bhaskar.doorstep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bhaskar.doorstep.model.ProductDTO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class AddProductInDb extends AppCompatActivity {
    EditText name,category,productTypeId,description,quantityType,isRequired;
    String Sname,Scategory,SproductTypeId,Sdescription,SquantityType,SisRequired;
    TextView title;
    ImageView back_btn;
    Button add_product_btn;
    private String TAG="AddProductInDb";
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_in_db);
        //intialize variable
        name=findViewById(R.id.add_product_name);
        category=findViewById(R.id.add_product_category);
        productTypeId=findViewById(R.id.add_product_productTypeId);
        description=findViewById(R.id.add_product_description);
        quantityType=findViewById(R.id.add_product_quantity_type);
        isRequired=findViewById(R.id.add_product_isrequired);
        title=findViewById(R.id.add_product_title);
        back_btn=findViewById(R.id.add_product_back_btn);
        add_product_btn=findViewById(R.id.add_product_button);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();




        add_product_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkTextInput())
                {
                    Log.d(TAG,"input Verification Passed");
                    addInDb();

                }
                else {
                    Log.d(TAG,"input Verification Failed");
                }



            }
        });

    }

    private void addInDb() {
        Log.d(TAG,"inside addInDb");
        Random random=new Random();
        String id=String.valueOf(random.nextInt(99999));
        Boolean isReq=true;
        ProductDTO productDTO=new ProductDTO(id,Sname,Scategory,SproductTypeId,Sdescription, "ImageUrl",SquantityType, isReq,"supplierName", "supplierId") ;
        firebaseFirestore.collection("prod").document("product").collection(productDTO.getCategory()).document(productDTO.getName()).set(productDTO).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG,"onSuccess firebase added");
                Toast.makeText(AddProductInDb.this, "Succesfully added ", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"failure exception msg= "+e.getMessage());
                Toast.makeText(AddProductInDb.this, "Failed ", Toast.LENGTH_SHORT).show();
            }
        });




    }

    private boolean checkTextInput() {
        Sname= name.getText().toString();
        Scategory=category.getText().toString();
        SproductTypeId=productTypeId.getText().toString();
        Sdescription=description.getText().toString();
        SquantityType=quantityType.getText().toString();
        SisRequired=isRequired.getText().toString();

        Log.d(TAG,"inside checkTextInput");
        Log.d(TAG,"Sname= "+Sname+" Scategory= "+Scategory+" SproductTypeId="+SproductTypeId+" Sdescription= "+Sdescription+" SquantityType= "+SquantityType+" SisRequired= "+SisRequired);

        if(Sname.isEmpty())
        {
            name.setError("Name Required");
            name.requestFocus();
            return false;
        }
        if(Scategory.isEmpty())
        {
            category.setError("Category Required");
            category.requestFocus();
            return false;
        }
        if(SproductTypeId.isEmpty())
        {
            productTypeId.setError("product type id Required");
            productTypeId.requestFocus();
            return false;
        }
        if(Sdescription.isEmpty())
        {
            description.setError("description  Required");
            description.requestFocus();
            return false;
        }
        if(SquantityType.isEmpty())
        {
            quantityType.setError("quantity Type Required");
            quantityType.requestFocus();
            return false;
        }
        if(SisRequired.isEmpty())
        {
            isRequired.setError("This is  Required");
            isRequired.requestFocus();
            return false;
        }

        //if everyThing is Correct

        Log.d(TAG,"inside checkTextInput everyThing Is correct");
        return true;

    }
}