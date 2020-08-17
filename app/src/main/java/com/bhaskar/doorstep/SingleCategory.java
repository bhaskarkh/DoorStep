package com.bhaskar.doorstep;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bhaskar.doorstep.adapter.SingleCategoryAdapter;
import com.bhaskar.doorstep.model.RecentlyViewed;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_category);

        singleCategoryBack=findViewById(R.id.singleCategoryBack);
        singleCategoryRecycler=findViewById(R.id.single_category);
        singleCategoryBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SingleCategory.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        // adding data to model
        //(String name, String description, String price, int imageUrl, int bigimageurl)
        singleCategoryItemsList = new ArrayList<>();
        singleCategoryItemsList.add(new RecentlyViewed("Watermelon", "Watermelon has high water content and also provides some fiber.", "₹ 80", "1", "KG", card4, b4));
        singleCategoryItemsList.add(new RecentlyViewed("Papaya", "Papayas are spherical or pear-shaped fruits that can be as long as 20 inches.", "₹ 85", "1", "KG", card3, b3));
        singleCategoryItemsList.add(new RecentlyViewed("Strawberry", "The strawberry is a highly nutritious fruit, loaded with vitamin C.", "₹ 30", "1", "KG", card2, b1));
        singleCategoryItemsList.add(new RecentlyViewed("Kiwi", "Full of nutrients like vitamin C, vitamin K, vitamin E, folate, and potassium.", "₹ 30", "1", "PC", card1, b2));
        singleCategoryItemsList.add(new RecentlyViewed("Watermelon", "Watermelon has high water content and also provides some fiber.", "₹ 80", "1", "KG", card4, b4));
        singleCategoryItemsList.add(new RecentlyViewed("Papaya", "Papayas are spherical or pear-shaped fruits that can be as long as 20 inches.", "₹ 85", "1", "KG", card3, b3));
        singleCategoryItemsList.add(new RecentlyViewed("Strawberry", "The strawberry is a highly nutritious fruit, loaded with vitamin C.", "₹ 30", "1", "KG", card2, b1));
        singleCategoryItemsList.add(new RecentlyViewed("Kiwi", "Full of nutrients like vitamin C, vitamin K, vitamin E, folate, and potassium.", "₹ 30", "1", "PC", card1, b2));




        setCategoryRecycler(singleCategoryItemsList);

    }


    private void setCategoryRecycler(List<RecentlyViewed> singleCategoryItemsList) {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        singleCategoryRecycler.setLayoutManager(layoutManager);
        singleCategoryRecycler.addItemDecoration(new SingleCategory.GridSpacingItemDecoration(2, dpToPx(16), true));
        singleCategoryRecycler.setItemAnimator(new DefaultItemAnimator());
        singleCategoryAdapter=new SingleCategoryAdapter(this,singleCategoryItemsList);
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