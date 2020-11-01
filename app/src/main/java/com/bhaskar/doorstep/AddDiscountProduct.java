package com.bhaskar.doorstep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bhaskar.doorstep.adapter.AddDiscountProductAdapter;
import com.bhaskar.doorstep.adapter.SingleCategoryAdapter;
import com.bhaskar.doorstep.allinterface.ProductInterface;
import com.bhaskar.doorstep.model.ProductDTO;
import com.bhaskar.doorstep.services.Home;
import com.bhaskar.doorstep.services.ProductServices;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AddDiscountProduct extends AppCompatActivity implements ProductInterface {
    Spinner add_discount_cat;
    Home home;
    ProductServices productServices;
    String category;
    FirebaseDatabase firebaseDatabase;
    RecyclerView productRecyclerView;
    AddDiscountProductAdapter addDiscountProductAdapter;
    ProgressBar add_discount_product_progressbar;
    private static final String TAG = "AddDiscountProduct";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_discount_product);
        add_discount_cat=findViewById(R.id.add_discount_cat);
        productRecyclerView=findViewById(R.id.add_discount_prod_recyclerview);
        add_discount_product_progressbar=findViewById(R.id.add_discount_product_progressbar);
        add_discount_product_progressbar.setVisibility(View.VISIBLE);
        
        home=new Home(this);
        firebaseDatabase=FirebaseDatabase.getInstance();
        productServices=new ProductServices(this);
        productServices.setProductInterface(this);

        setAllSpinnerValue();
        
        add_discount_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {

                category = ((TextView) v.findViewById(R.id.spinner_name)).getText().toString();
                Log.d(TAG, "onItemSelected: Scategory="+category);
                fetchDiscountProduct(category);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                Log.d(TAG, "onItemSelected: No Category Selected ");

            }

        });
    }

    private void fetchDiscountProduct(String category) {
        Log.d(TAG, "fetchDiscountProduct: category="+category);

        productServices.getProductListByCategory(category,firebaseDatabase);

    }

    private void setAllSpinnerValue() {

        String[] catList = AddDiscountProduct.this.getResources().getStringArray(R.array.category_array);
        String[] newCatArray=new String[catList.length+1];
        newCatArray[0]="All Category";
        for (int i=1;i<newCatArray.length;i++)
        {
            newCatArray[i]=catList[i-1];

        }
        home.setSpinnerAdapterForTextOnly(newCatArray,add_discount_cat,false);
    }

    private void setCategoryRecycler(List<ProductDTO> productDTOList) {
        productServices.sortProductList(productDTOList);
        add_discount_product_progressbar.setVisibility(View.GONE);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        productRecyclerView.setLayoutManager(layoutManager);
        productRecyclerView.setItemAnimator(new DefaultItemAnimator());
        addDiscountProductAdapter =new AddDiscountProductAdapter(this,productDTOList,productServices);
        productRecyclerView.setAdapter(addDiscountProductAdapter);

    }
    @Override
    public void onProductAddedinDb() {
        //get ProductList Here


    }

    @Override
    public void onProductAddFailed() {

    }

    @Override
    public void setProductListToRecyclerView(List<ProductDTO> productDTOList) {
        setCategoryRecycler(productDTOList);
    }
}