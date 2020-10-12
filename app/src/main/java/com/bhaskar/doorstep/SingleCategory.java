package com.bhaskar.doorstep;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bhaskar.doorstep.adapter.SingleCategoryAdapter;
import com.bhaskar.doorstep.model.ProductDTO;
import com.bhaskar.doorstep.model.RecentlyViewed;
import com.bhaskar.doorstep.util.Home;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

public class SingleCategory extends AppCompatActivity {
    RecyclerView singleCategoryRecycler;
    SingleCategoryAdapter singleCategoryAdapter;
    List<RecentlyViewed> singleCategoryItemsList;
    ImageView singleCategoryBack;
    FirebaseDatabase firebaseDatabase;
    TextView singleCategoryTitle;
     final String TAG="SingleCategory";
     ProgressBar single_category_progressBar;
     Home home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_category);

        singleCategoryBack=findViewById(R.id.singleCategoryBack);
        singleCategoryRecycler=findViewById(R.id.single_category);
        singleCategoryTitle=findViewById(R.id.singleCategoryTitle);
        single_category_progressBar=findViewById(R.id.single_category_progressBar);
        firebaseDatabase=FirebaseDatabase.getInstance();
        singleCategoryRecycler.setNestedScrollingEnabled(false);
        home=new Home(this);
        singleCategoryBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               home.gotToHome();
            }
        });
        String categoryName=getIntent().getStringExtra("cat_name");
             if(categoryName!=null)
             {
                 singleCategoryTitle.setText(categoryName);

                 getCategoryItemList(categoryName);
             }
             else {
                 Toast.makeText(SingleCategory.this, "Some error occured try again", Toast.LENGTH_LONG).show();
             }
        single_category_progressBar.setVisibility(View.VISIBLE);
    }

    private void getCategoryItemList(final String categoryName) {
       final List<ProductDTO> productDTOList=new ArrayList<>();
        DatabaseReference databaseReference=firebaseDatabase.getReference().child("test").child("product");


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Log.d(TAG,"db key= "+dataSnapshot.getKey()+" our category categoryName= "+categoryName);
                    if(dataSnapshot.getKey().equals(categoryName)) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            ProductDTO productDTO = dataSnapshot1.getValue(ProductDTO.class);
                            productDTOList.add(productDTO);

                        }
                    }
                }

                setCategoryRecycler(productDTOList);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void setCategoryRecycler(List<ProductDTO> productDTOList) {
        single_category_progressBar.setVisibility(View.GONE);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        singleCategoryRecycler.setLayoutManager(layoutManager);
        singleCategoryRecycler.setItemAnimator(new DefaultItemAnimator());
        singleCategoryAdapter=new SingleCategoryAdapter(this,productDTOList);
        singleCategoryRecycler.setAdapter(singleCategoryAdapter);

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