package com.bhaskar.doorstep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bhaskar.doorstep.adapter.AddDiscountRecentlyProductAdapter;
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
    AddDiscountRecentlyProductAdapter addDiscountRecentlyProductAdapter;
    ProgressBar add_discount_product_progressbar;
    ImageView add_discount_product_back_btn;
    TextView header_title;
    private static final String TAG = "AddDiscountProduct";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_discount_product);
        add_discount_cat=findViewById(R.id.add_discount_cat);
        productRecyclerView=findViewById(R.id.add_discount_prod_recyclerview);
        add_discount_product_back_btn=findViewById(R.id.header_back_btn_image);
        add_discount_product_progressbar=findViewById(R.id.add_discount_product_progressbar);
        header_title=findViewById(R.id.header_title);
        header_title.setText("Add Discount Product");

        add_discount_product_progressbar.setVisibility(View.VISIBLE);
        
        home=new Home(this);
        firebaseDatabase=FirebaseDatabase.getInstance();
        productServices=new ProductServices(this);
        productServices.setProductInterface(this);

        setAllSpinnerValue();

        add_discount_product_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.IntentActivityStart(AdminDashboard.class);
            }
        });
        
        add_discount_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {

                if(v!=null) {
                    category = ((TextView) v.findViewById(R.id.spinner_name)).getText().toString();
                    Log.d(TAG, "onItemSelected: Scategory=" + category);
                    fetchDiscountProduct(category);
                }
                else
                {
                    fetchDiscountProduct("All Category");

                }

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
        addDiscountRecentlyProductAdapter =new AddDiscountRecentlyProductAdapter(this,productDTOList,productServices,"discount");
        productRecyclerView.setAdapter(addDiscountRecentlyProductAdapter);

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

    @Override
    public void setDiscountProductListToRecyclerView(List<ProductDTO> discountProductDTOList) {

    }

    @Override
    public void setRecentlyViewProductListToRecyclerView(List<ProductDTO> recentlyViewProductDTOList) {

    }
}