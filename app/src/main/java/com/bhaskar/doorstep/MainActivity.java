package com.bhaskar.doorstep;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bhaskar.doorstep.adapter.CategoryAdapter;
import com.bhaskar.doorstep.adapter.DiscountedProductAdapter;
import com.bhaskar.doorstep.adapter.RecentlyViewedAdapter;
import com.bhaskar.doorstep.adapter.SliderAdapterExample;
import com.bhaskar.doorstep.model.Category;
import com.bhaskar.doorstep.model.DiscountedProducts;
import com.bhaskar.doorstep.model.GoogleSignInDTO;
import com.bhaskar.doorstep.model.RecentlyViewed;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import static com.bhaskar.doorstep.R.drawable.abc_cab_background_internal_bg;
import static com.bhaskar.doorstep.R.drawable.b1;
import static com.bhaskar.doorstep.R.drawable.b2;
import static com.bhaskar.doorstep.R.drawable.b3;
import static com.bhaskar.doorstep.R.drawable.b4;
import static com.bhaskar.doorstep.R.drawable.card1;
import static com.bhaskar.doorstep.R.drawable.card2;
import static com.bhaskar.doorstep.R.drawable.card3;
import static com.bhaskar.doorstep.R.drawable.card4;
import static com.bhaskar.doorstep.R.drawable.discountberry;
import static com.bhaskar.doorstep.R.drawable.discountbrocoli;
import static com.bhaskar.doorstep.R.drawable.discountmeat;
import static com.bhaskar.doorstep.R.drawable.ic_home_fish;
import static com.bhaskar.doorstep.R.drawable.ic_home_fruits;
import static com.bhaskar.doorstep.R.drawable.ic_home_meats;
import static com.bhaskar.doorstep.R.drawable.ic_home_veggies;

public class MainActivity extends AppCompatActivity {

    RecyclerView discountRecyclerView, categoryRecyclerView, recentlyViewedRecycler;
    DiscountedProductAdapter discountedProductAdapter;
    List<DiscountedProducts> discountedProductsList;

    CategoryAdapter categoryAdapter;
    List<Category> categoryList;

    RecentlyViewedAdapter recentlyViewedAdapter;
    List<RecentlyViewed> recentlyViewedList;

    TextView allCategory;
    ImageView account_setting,profile_pic,main_cart;
    private GoogleSignInClient mGoogleSignInClient;
    GoogleSignInDTO googleSignInDTO;
    FirebaseAuth fAuth;
    SliderView sliderView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        discountRecyclerView = findViewById(R.id.discountedRecycler);
        categoryRecyclerView = findViewById(R.id.categoryRecycler);
        allCategory = findViewById(R.id.allCategoryImage);
        recentlyViewedRecycler = findViewById(R.id.recently_item);
        account_setting=findViewById(R.id.account_setting);
        profile_pic=findViewById(R.id.profile_pic);
        main_cart=findViewById(R.id.main_cart);
        sliderView=findViewById(R.id.imageSlider);
        fAuth=FirebaseAuth.getInstance();
        SliderAdapterExample sadapter = new SliderAdapterExample(this);

        sliderView.setSliderAdapter(sadapter);

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();


        googleSignInDTO=getUserDetailFromGoogle();
        Log.d("MainActivity","googleSignInDTO to String= "+googleSignInDTO.toString());
        createGoogleSignInRequest();
      Glide.with(MainActivity.this).load(String.valueOf(googleSignInDTO.getUserPhoto())).circleCrop().into(profile_pic);

       // Glide.with(this).load(String.valueOf(googleSignInDTO.getUserPhoto())).into(profile_pic);

        account_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
               mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                 @Override
                 public void onComplete(@NonNull Task<Void> task) {
                     Intent i = new Intent(MainActivity.this, LoginScreen.class);
                     startActivity(i);
                 }
             });

            }
        });


        allCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AllCategory.class);
                startActivity(i);
            }
        });

        main_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,EnterMobileNumber.class);
                i.putExtra("fuid",fAuth.getCurrentUser().getUid());
                startActivity(i);
            }
        });

        // adding data to model
        discountedProductsList = new ArrayList<>();
        discountedProductsList.add(new DiscountedProducts(1, discountberry));
        discountedProductsList.add(new DiscountedProducts(2, discountbrocoli));
        discountedProductsList.add(new DiscountedProducts(3, discountmeat));
        discountedProductsList.add(new DiscountedProducts(4, discountberry));
        discountedProductsList.add(new DiscountedProducts(5, discountbrocoli));
        discountedProductsList.add(new DiscountedProducts(6, discountmeat));

        // adding data to model
        //jcdj
        categoryList = new ArrayList<>();
        categoryList.add(new Category(1, ic_home_fruits));
        categoryList.add(new Category(2, ic_home_fish));
        categoryList.add(new Category(3, ic_home_meats));
        categoryList.add(new Category(4, ic_home_veggies));
        categoryList.add(new Category(5, ic_home_fruits));
        categoryList.add(new Category(6, ic_home_fish));
        categoryList.add(new Category(7, ic_home_meats));
        categoryList.add(new Category(8, ic_home_veggies));
        categoryList.add(new Category(9, ic_home_veggies));
        categoryList.add(new Category(10, ic_home_fruits));
        categoryList.add(new Category(11, ic_home_fish));
        categoryList.add(new Category(12, ic_home_meats));
        categoryList.add(new Category(13, ic_home_veggies));
        categoryList.add(new Category(14, ic_home_fish));


        // adding data to model
       recentlyViewedList = new ArrayList<>();
       recentlyViewedList.add(new RecentlyViewed("Watermelon", "Watermelon has high water content and also provides some fiber.", "₹ 80", "1", "KG", card4, b4));
       recentlyViewedList.add(new RecentlyViewed("Papaya", "Papayas are spherical or pear-shaped fruits that can be as long as 20 inches.", "₹ 85", "1", "KG", card3, b3));
       recentlyViewedList.add(new RecentlyViewed("Strawberry", "The strawberry is a highly nutritious fruit, loaded with vitamin C.", "₹ 30", "1", "KG", card2, b1));
       recentlyViewedList.add(new RecentlyViewed("Kiwi", "Full of nutrients like vitamin C, vitamin K, vitamin E, folate, and potassium.", "₹ 30", "1", "PC", card1, b2));

        setDiscountedRecycler(discountedProductsList);
        setCategoryRecycler(categoryList);
       setRecentlyViewedRecycler(recentlyViewedList);

    }

    private void setDiscountedRecycler(List<DiscountedProducts> dataList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        discountRecyclerView.setLayoutManager(layoutManager);
        discountedProductAdapter = new DiscountedProductAdapter(this,dataList);
        discountRecyclerView.setAdapter(discountedProductAdapter);
    }


    private void setCategoryRecycler(List<Category> categoryDataList) {
        /*RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        categoryRecyclerView.setLayoutManager(layoutManager);
        categoryAdapter = new CategoryAdapter(this,categoryDataList);
        categoryRecyclerView.setAdapter(categoryAdapter);*/


        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 4);
        categoryRecyclerView.setLayoutManager(layoutManager);
       categoryRecyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        categoryAdapter = new CategoryAdapter(this,categoryDataList);
        categoryRecyclerView.setAdapter(categoryAdapter);

    }

    private void setRecentlyViewedRecycler(List<RecentlyViewed> recentlyViewedDataList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recentlyViewedRecycler.setLayoutManager(layoutManager);
        recentlyViewedAdapter = new RecentlyViewedAdapter(this,recentlyViewedDataList);
        recentlyViewedRecycler.setAdapter(recentlyViewedAdapter);
    }
    //Now again we need to create a adapter and model class for recently viewed items.
    // lets do it fast.

    private void createGoogleSignInRequest() {
// Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    public GoogleSignInDTO getUserDetailFromGoogle()
    {
        GoogleSignInDTO googleSignInDTO=new GoogleSignInDTO();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
        if (acct != null) {
            googleSignInDTO.setUserName(acct.getDisplayName());
            googleSignInDTO.setUserGivenName(acct.getGivenName());
            googleSignInDTO.setUserFamilyName(acct.getFamilyName());
            googleSignInDTO.setUserEmail(acct.getEmail());
            googleSignInDTO.setUserId(acct.getId());
            googleSignInDTO.setUserPhoto(acct.getPhotoUrl());
        }

        return googleSignInDTO;
    }


    // now we need some item decoration class for manage spacing

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
