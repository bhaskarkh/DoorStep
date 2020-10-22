package com.bhaskar.doorstep;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.bhaskar.doorstep.model.ProductDTO;
import com.bhaskar.doorstep.model.RecentlyViewed;
import com.bhaskar.doorstep.model.SliderItem;
import com.bhaskar.doorstep.services.Home;
import com.bhaskar.doorstep.services.MySharedPreferences;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.FirebaseDatabase;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import static com.bhaskar.doorstep.R.drawable.b1;
import static com.bhaskar.doorstep.R.drawable.b2;
import static com.bhaskar.doorstep.R.drawable.b3;
import static com.bhaskar.doorstep.R.drawable.b4;
import static com.bhaskar.doorstep.R.drawable.card1;
import static com.bhaskar.doorstep.R.drawable.card2;
import static com.bhaskar.doorstep.R.drawable.card3;
import static com.bhaskar.doorstep.R.drawable.card4;
import static com.bhaskar.doorstep.R.drawable.discountbrocoli;
import static com.bhaskar.doorstep.R.drawable.discountmeat;
import static com.bhaskar.doorstep.R.drawable.ic_home_fish;
import static com.bhaskar.doorstep.R.drawable.ic_home_fruits;
import static com.bhaskar.doorstep.R.drawable.ic_home_veggies;

public class MainActivity extends AppCompatActivity {

    RecyclerView discountRecyclerView, categoryRecyclerView, recentlyViewedRecycler;
    DiscountedProductAdapter discountedProductAdapter;
    List<DiscountedProducts> discountedProductsList;

    CategoryAdapter categoryAdapter;
    List<Category> categoryList;

    RecentlyViewedAdapter recentlyViewedAdapter;
    List<RecentlyViewed> recentlyViewedList;
    SliderAdapterExample sadapter;
    List<ProductDTO> productDTOList;

    TextView allCategory;
    ImageView account_setting,profile_pic,main_cart;
    private GoogleSignInClient mGoogleSignInClient;
    GoogleSignInDTO googleSignInDTO;
    FirebaseAuth fAuth;
    FirebaseDatabase firebaseDatabase;
    SliderView sliderView;

    private String TAG="MainActivity";
    List<String> sliderUrlList;
    ProgressBar discount_list_progressbar;
    MySharedPreferences mySharedPreferences;
    String Loginsource;
    Home home;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        discountRecyclerView = findViewById(R.id.discountedRecycler);
        categoryRecyclerView = findViewById(R.id.categoryRecycler);
        recentlyViewedRecycler = findViewById(R.id.recently_item);
        // disabling nested scroll view for fast scrolling
        discountRecyclerView.setNestedScrollingEnabled(false);
        categoryRecyclerView.setNestedScrollingEnabled(false);
        recentlyViewedRecycler.setNestedScrollingEnabled(false);

        allCategory = findViewById(R.id.allCategoryImage);
        account_setting=findViewById(R.id.account_setting);
        profile_pic=findViewById(R.id.profile_pic);
        main_cart=findViewById(R.id.main_cart);
        sliderView=findViewById(R.id.imageSlider);
        discount_list_progressbar=findViewById(R.id.discount_list_progressbar);
        discount_list_progressbar.setVisibility(View.GONE);

        fAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        home=new Home(this);
        mySharedPreferences=new MySharedPreferences(this);
        //slider
        Log.d(TAG,"fuid in main= "+fAuth.getCurrentUser().getUid());
        sadapter= new SliderAdapterExample(this);

        sliderView.setSliderAdapter(sadapter);

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();

        renewItems();
        //slider
       Loginsource=mySharedPreferences.getLoginSourceToSharedPreference();

       if(Loginsource.equals("google")) {
           createGoogleSignInRequest();
       }

    Glide.with(MainActivity.this).load(mySharedPreferences.getUserDetailsFromSharedPreference().getUserPhoto()).circleCrop().into(profile_pic);



        account_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
               CheckLoginSourceAndSignOut();
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
                Intent i=new Intent(MainActivity.this,AdminDashboard.class);
                startActivity(i);
            }
        });

        // adding data to model
        discountedProductsList = new ArrayList<>();
        discountedProductsList.add(new DiscountedProducts(1, ic_home_fruits));
        discountedProductsList.add(new DiscountedProducts(2, ic_home_fish));
        discountedProductsList.add(new DiscountedProducts(3, ic_home_veggies));
        discountedProductsList.add(new DiscountedProducts(4, b4));
        discountedProductsList.add(new DiscountedProducts(5, discountbrocoli));
        discountedProductsList.add(new DiscountedProducts(6, discountmeat));

        // adding data to model
        //jcdj






        // adding data to model
       recentlyViewedList = new ArrayList<>();
       recentlyViewedList.add(new RecentlyViewed("Watermelon", "Watermelon has high water content and also provides some fiber.", "₹ 80", "1", "KG", card4, b4));
       recentlyViewedList.add(new RecentlyViewed("Papaya", "Papayas are spherical or pear-shaped fruits that can be as long as 20 inches.", "₹ 85", "1", "KG", card3, b3));
       recentlyViewedList.add(new RecentlyViewed("Strawberry", "The strawberry is a highly nutritious fruit, loaded with vitamin C.", "₹ 30", "1", "KG", card2, b1));
       recentlyViewedList.add(new RecentlyViewed("Kiwi", "Full of nutrients like vitamin C, vitamin K, vitamin E, folate, and potassium.", "₹ 30", "1", "PC", card1, b2));

      //  getProductList();
        getCategoryList();
        setDiscountedRecycler(discountedProductsList);

       setRecentlyViewedRecycler(recentlyViewedList);

    }

    private void CheckLoginSourceAndSignOut() {
        Log.d(TAG, "CheckLoginSourceAndSignOut: Loginsource= "+Loginsource);
        if(Loginsource.equals("google")) {
            Log.d(TAG, "CheckLoginSourceAndSignOut: google login");
            FirebaseAuth.getInstance().signOut();
            mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(MainActivity.this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, LoginScreen.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }
            });
        }
        if (Loginsource.equals("email"))
        {
            Log.d(TAG, "CheckLoginSourceAndSignOut: email login");
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(MainActivity.this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(MainActivity.this, LoginScreen.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();

        }
        else {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(MainActivity.this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(MainActivity.this, LoginScreen.class);
            startActivity(i);
            finish();

        }

    }

    private void getCategoryList() {
        categoryList = new ArrayList<>();
        String[] category_array=MainActivity.this.getResources().getStringArray(R.array.category_array);
       int i=1;
        for(String categoryName:category_array)
        {
            int imageName=home.getImageNameFromCategory(categoryName);
            categoryList.add(new Category(i,imageName,categoryName));
            i++;


        }
        Log.d(TAG,"getCategoryList size of List= "+categoryList.size());


        setCategoryRecycler(categoryList);
    }




    private List<String> getSliderUrlList() {
        List<String> sList=new ArrayList<>();


        return sList;
    }


    private void setDiscountedRecycler(List<DiscountedProducts> dataList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        discountRecyclerView.setLayoutManager(layoutManager);
        discountedProductAdapter = new DiscountedProductAdapter(this,dataList);
        discountRecyclerView.setAdapter(discountedProductAdapter);
    }


    private void setCategoryRecycler(List<Category> categoryDataList) {
     RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 4);
        categoryRecyclerView.setLayoutManager(layoutManager);
      // categoryRecyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
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

/*

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
*/


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


    //Image Slider method

    public void renewItems() {

        List<SliderItem> sliderItemList = new ArrayList<>();
        sliderUrlList=getSliderUrlList();
        //dummy data
        for (int i = 0; i < 5; i++) {
            SliderItem sliderItem = new SliderItem();
            sliderItem.setDescription("Slider Item " + i);
            switch (i)
            {
                case 0:
                    sliderItem.setImageUrl("https://images.freekaamaal.com/post_images/1576047645.png");
                    sliderItem.setDescription("slide 0");
                    break;
                case 1:
                    sliderItem.setImageUrl("https://www.winstar.com.tw/uploads/photos/special-offer.jpg");
                    sliderItem.setDescription("slide 1");
                    break;
                case 2:
                    sliderItem.setImageUrl("https://image.shutterstock.com/image-vector/special-offer-banner-vector-format-260nw-717480880.jpg");
                    sliderItem.setDescription("slide 2");
                    break;
                case 3:
                    sliderItem.setImageUrl("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
                    sliderItem.setDescription("slide 3");
                    break;
                case 4:
                    sliderItem.setImageUrl("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
                    sliderItem.setDescription("slide 4");
                    break;

            }

            sliderItemList.add(sliderItem);
        }
        sadapter.renewItems(sliderItemList);
    }

    public void removeLastItem() {
        if (sadapter.getCount() - 1 >= 0)
            sadapter.deleteItem(sadapter.getCount() - 1);
    }

    public void addNewItem() {
        SliderItem sliderItem = new SliderItem();
        sliderItem.setDescription("Slider Item Added Manually");
        sliderItem.setImageUrl("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        sadapter.addItem(sliderItem);
    }

    @Override
    public void onBackPressed() {

        Dialog epicDialog=new Dialog(this);
        epicDialog.setContentView(R.layout.exit_layout);
        ImageView btnYes=epicDialog.findViewById(R.id.yesIdOnExit);
        ImageView btnNo=epicDialog.findViewById(R.id.noIdOnExit);
        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicDialog.show();
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(FragmentMain.this,"Yes",Toast.LENGTH_SHORT).show();
                epicDialog.dismiss();
                finish();

            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(FragmentMain.this,"No",Toast.LENGTH_SHORT).show();
                epicDialog.dismiss();

            }
        });

    }
}
