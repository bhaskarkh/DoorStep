package com.bhaskar.doorstep;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Scroller;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bhaskar.doorstep.allinterface.ProductInterface;
import com.bhaskar.doorstep.model.ProductDTO;
import com.bhaskar.doorstep.services.ProductServices;
import com.bhaskar.doorstep.services.Home;
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

public class AddProductInDb extends AppCompatActivity implements ProductInterface {
    EditText name,category,productTypeId,description,price;
    String Sname,Scategory,SproductTypeId,Sdescription,SquantityType,SisRequired,Sprice,SisDiscount;
    String id="0";
    String imageUrlFromStorage;
    TextView title;
    ImageView back_btn,upload_image_preview;
    Button add_product_btn;
    private String TAG="AddProductInDb";
    Home hm;
    ProductServices productServices;
   // FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    Spinner spinnercat,quantityType,isRequired,add_product_discount;
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
        price=findViewById(R.id.add_product_price);
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
        add_product_discount=findViewById(R.id.add_product_discount);

      //  firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        storageReference=FirebaseStorage.getInstance().getReference("product");
       // id= String.valueOf(System.currentTimeMillis());
        hm=new Home(this);
        productServices=new ProductServices(this);

        description.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(final View v, final MotionEvent motionEvent) {
                if (v.getId() == R.id.add_product_description) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(
                                    false);
                            break;
                    }
                }
                return false;
            }
        });

        setValueInSpinner();

        spinnercat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {

                Scategory = ((TextView) v.findViewById(R.id.spinner_name)).getText().toString();
                Log.d(TAG, "onItemSelected: Scategory="+Scategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                Log.d(TAG, "onItemSelected: No Category Selected ");

            }

        });


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
                        uploadImageInFireStorage();
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

    private void setValueInSpinner() {
        String[] discountArray={"No","Yes"};

         String[] quantityTypeList=AddProductInDb.this.getResources().getStringArray(R.array.quantity_type_array);
         String[] isRequiredList=AddProductInDb.this.getResources().getStringArray(R.array.is_required_array);
         String[] catList=AddProductInDb.this.getResources().getStringArray(R.array.category_array);
          hm.setSpinnerAdapterForTextOnly(quantityTypeList,quantityType,true);
          hm.setSpinnerAdapterForTextOnly(isRequiredList,isRequired,true);
          hm.setSpinnerAdapterForTextOnly(catList,spinnercat,false);
          hm.setSpinnerAdapterForTextOnly(discountArray,add_product_discount,true);

    }




    private void clearAllInput() {
        name.setText(null);
        productTypeId.setText(null);
        price.setText(null);
        description.setText(null);
        upload_image_preview.setImageResource(R.drawable.ic_no_image_selected);
        image_upload_progressbar.setVisibility(View.GONE);

    }


    private boolean checkTextInput() {
        Sname= name.getText().toString();
        SproductTypeId=productTypeId.getText().toString();
        Sdescription=description.getText().toString();
        Sprice=price.getText().toString();
        SquantityType=String.valueOf(quantityType.getSelectedItem());
        SisRequired=String.valueOf(isRequired.getSelectedItemId());
        SisDiscount=String.valueOf(add_product_discount.getSelectedItemId());






        Log.d(TAG,"inside checkTextInput");

        if(Sname.isEmpty())
        {
            name.setError("Name Required");
            name.requestFocus();
            return false;
        }
        if(Scategory.isEmpty())
        {
            Toast.makeText(this, "Select Category", Toast.LENGTH_SHORT).show();
            spinnercat.requestFocus();
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
        if(Sprice.isEmpty())
        {
            price.setError("Price  Required");
            price.requestFocus();
            return false;

        }
        if(SquantityType.isEmpty())
        {
            Toast.makeText(this, "Select Quantity Type", Toast.LENGTH_SHORT).show();
            quantityType.requestFocus();
            return false;
        }
        if(SisRequired.isEmpty())
        {
            isRequired.requestFocus();
            return false;
        }

        //if everyThing is Correct

        Log.d(TAG,"Sname= "+Sname+" Scategory= "+Scategory+" SproductTypeId="+SproductTypeId+" Sdescription= "+Sdescription+" SquantityType= "+SquantityType+" SisRequired= "+SisRequired);

        Log.d(TAG,"inside checkTextInput everyThing Is correct");
        return true;

    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cR=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImageInFireStorage(){
        if(mImageUri!=null)
        {
            image_upload_progressbar.setVisibility(View.VISIBLE);
            upload_product_progressbar.setVisibility(View.VISIBLE);

            StorageReference fileReference=storageReference.child(Scategory+"/"+Sname+"."+getFileExtension(mImageUri));
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

                            Task<Uri> firebaseUri=taskSnapshot.getStorage().getDownloadUrl();
                            firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageUrlFromStorage=uri.toString();
                                    Log.d(TAG, "onSuccess: image url updated as ="+imageUrlFromStorage);

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

    private void addInDb() {
        Boolean isReq=false;
        Boolean isDiscount=false;

        if(SisRequired.equals("1")) //0 means yes 1 means No
        {
            isReq=true;
        }
        Log.d(TAG, "addInDb: SisDiscount= "+SisDiscount);
        if(SisDiscount.equals("1")) //0 means No 1 means Yes
        {
            isDiscount=true;

        }
        id= String.valueOf(System.currentTimeMillis());
        ProductDTO productDTO=new ProductDTO(id,Sname,Scategory,SproductTypeId,Sdescription, imageUrlFromStorage,SquantityType, isReq,"supplierName", "supplierId",1,Double.valueOf(Sprice),150,true,1,isDiscount) ;
        productServices.setProductInterface(this);
        productServices.addProductInDb(productDTO,firebaseDatabase);

    }

    @Override
    public void onProductAddedinDb() {
        upload_product_progressbar.setVisibility(View.GONE);
        Toast.makeText(AddProductInDb.this, "Succesfully added ", Toast.LENGTH_SHORT).show();
        clearAllInput();
    }

    @Override
    public void onProductAddFailed() {
        image_upload_progressbar.setVisibility(View.GONE);
        upload_product_progressbar.setVisibility(View.GONE);
        Toast.makeText(this, "Failed To upload Product Try again", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setProductListToRecyclerView(List<ProductDTO> productDTOList) {

    }

}