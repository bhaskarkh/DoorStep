package com.bhaskar.doorstep;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bhaskar.doorstep.model.ProductDTO;
import com.bhaskar.doorstep.util.Home;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;


import java.util.List;
import java.util.Random;

public class AddProductInDb extends AppCompatActivity {
    EditText name,category,productTypeId,description,quantityType,isRequired;
    String Sname,Scategory,SproductTypeId,Sdescription,SquantityType,SisRequired;
    String id="0";
    String imageUrlFromStorage;
    TextView title;
    ImageView back_btn,upload_image_preview;
    Button add_product_btn;
    private String TAG="AddProductInDb";
    Home hm;
   // FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    Spinner spinnercat;
    Button upload_product_image_btn;
    StorageReference storageReference;
    StorageTask uploadTask;
    ProgressBar image_upload_progressbar,upload_product_progressbar;
    private static final int PICK_IMAGE_REQUEST=1;
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_in_db);
        //intialize variable
        name=findViewById(R.id.add_product_name);
        spinnercat=findViewById(R.id.add_product_category);
        productTypeId=findViewById(R.id.add_product_productTypeId);
        description=findViewById(R.id.add_product_description);
        quantityType=findViewById(R.id.add_product_quantity_type);
        isRequired=findViewById(R.id.add_product_isrequired);
        title=findViewById(R.id.add_product_title);
        back_btn=findViewById(R.id.add_product_back_btn);
        add_product_btn=findViewById(R.id.add_product_button);
        upload_product_image_btn=findViewById(R.id.upload_product_image_btn);
        upload_image_preview=findViewById(R.id.upload_image_preview);
        image_upload_progressbar=findViewById(R.id.image_upload_progressbar);
        upload_product_progressbar=findViewById(R.id.upload_product_progressbar);

      //  firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        storageReference=FirebaseStorage.getInstance().getReference("product");
        id= String.valueOf(System.currentTimeMillis());
        hm=new Home(this);

        setCategoryInSpinner();
        add_product_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkTextInput())
                {
                    Log.d(TAG,"input Verification Passed");
                    if(uploadTask!=null && uploadTask.isInProgress())
                    {
                        Toast.makeText(AddProductInDb.this, "Upload is in Progress wait...", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        uploadFile();
                    }

                }
                else {
                    Log.d(TAG,"input Verification Failed");
                }



            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hm.gotToHome();
            }
        });

        upload_product_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

    }

    private void openFileChooser() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST &&resultCode==RESULT_OK
        && data!=null && data.getData()!=null)
        {
            mImageUri=data.getData();

            Glide.with(AddProductInDb.this).load(mImageUri).into(upload_image_preview);



        }
    }

    private void setCategoryInSpinner() {
        String[] catList=AddProductInDb.this.getResources().getStringArray(R.array.category_array);
        final ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>(this,R.layout.myspinnerstylehome,catList);
        dataAdapter.setDropDownViewResource(R.layout.myspinnerstylehome);
        spinnercat.setAdapter(dataAdapter);
    }

    private void addInDb() {
        Log.d(TAG,"inside addInDb");

        Boolean isReq=true;
        ProductDTO productDTO=new ProductDTO(id,Sname,Scategory,SproductTypeId,Sdescription, imageUrlFromStorage,SquantityType, isReq,"supplierName", "supplierId",1,100,150,true,1) ;


        firebaseDatabase.getReference().child("test").child("product").child(productDTO.getCategory()).child(productDTO.getName()).setValue(productDTO).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG,"onSuccess firebase added");
                upload_product_progressbar.setVisibility(View.GONE);
                Toast.makeText(AddProductInDb.this, "Succesfully added ", Toast.LENGTH_SHORT).show();
                clearAllInput();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"failure exception msg= "+e.getMessage());
                Toast.makeText(AddProductInDb.this, "Failed ", Toast.LENGTH_SHORT).show();
            }
        });





    }

    private void clearAllInput() {
        name.setText(null);
        productTypeId.setText(null);
        description.setText(null);
        description.setText(null);
        quantityType.setText(null);
        isRequired.setText(null);
        upload_image_preview.setImageResource(R.drawable.civil);
    }


    private boolean checkTextInput() {
        Sname= name.getText().toString();
        Scategory=String.valueOf(spinnercat.getSelectedItem());;
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

    private String getFileExtension(Uri uri)
    {
        ContentResolver cR=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(){
        if(mImageUri!=null)
        {

            StorageReference fileReference=storageReference.child(Scategory+"/"+id+"."+getFileExtension(mImageUri));
            uploadTask=fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    image_upload_progressbar.setProgress(0);
                                }
                            },2000);
                            Toast.makeText(AddProductInDb.this, "Upload SuccessFull", Toast.LENGTH_SHORT).show();
                            Task<Uri> firebaseUri=taskSnapshot.getStorage().getDownloadUrl();
                            firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageUrlFromStorage=uri.toString();
                                    Log.d(TAG, "onSuccess: image url updated as ="+imageUrlFromStorage);
                                    upload_product_progressbar.setVisibility(View.VISIBLE);
                                    addInDb();
                                }
                            });





                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: exception= "+e.getMessage());

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress=(100.0* snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                            Log.d(TAG, "onProgress: value="+progress);
                            image_upload_progressbar.setProgress((int)progress);

                        }
                    });

        }
        else{
            Toast.makeText(this, "Select Image of Product", Toast.LENGTH_SHORT).show();
        }
    }
}