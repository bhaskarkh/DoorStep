package com.bhaskar.doorstep.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bhaskar.doorstep.ProductDetails;
import com.bhaskar.doorstep.R;
import com.bhaskar.doorstep.model.ProductDTO;
import com.bhaskar.doorstep.model.RecentlyViewed;
import com.bhaskar.doorstep.services.Home;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import static maes.tech.intentanim.CustomIntent.customType;

public class RecentlyViewedAdapter extends RecyclerView.Adapter<RecentlyViewedAdapter.RecentlyViewedViewHolder> {

    Context context;
    List<ProductDTO> recentlyViewedList;
    public boolean showShimmer=true;
    private static final String TAG = "RecentlyViewedAdapter";

    public RecentlyViewedAdapter(Context context, List<ProductDTO> recentlyViewedList) {
        this.context = context;
        this.recentlyViewedList = recentlyViewedList;
    }

    @NonNull
    @Override
    public RecentlyViewedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recently_viewed_items, parent, false);

        return new RecentlyViewedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentlyViewedViewHolder holder, final int position) {

        if (showShimmer)
        {
            holder.shimmerFrameLayout.startShimmer();
        }
        else {
            holder.shimmerFrameLayout.stopShimmer();
            holder.shimmerFrameLayout.setShimmer(null);
            holder.image.setBackground(null);
            holder.name.setBackground(null);
            holder.name.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
            holder.name.requestLayout();
            holder.price.setBackground(null);
            holder.price.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
            holder.price.requestLayout();

            Home home = new Home(context);
            ProductDTO productDTO = recentlyViewedList.get(position);
            Log.d(TAG, "onBindViewHolder: productDTO= " + productDTO.getName());
            holder.name.setText(productDTO.getName());
            holder.price.setText(String.valueOf(productDTO.getPrice()));

            // home.loadImageInGlide(holder.bg,productDTO.getImage());
        /*Glide.with(context).load(productDTO.getImage()).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.bg.setBackground(resource); 
                }
            }
        });*/
            Log.d(TAG, "loadImageInGlide: ");
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.ic_no_image_available)
                    .error(R.drawable.ic_no_image_available)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.NORMAL);

            Glide.with(context).load(productDTO.getImage())
                    .apply(options)
                    .into(holder.image);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(context, ProductDetails.class);
                    i.putExtra("cat_name", productDTO.getCategory());
                    i.putExtra("selected_product", productDTO);
                    i.putExtra("source_to_product_details", "Home");

                    context.startActivity(i);


                }
            });
        }

    }

    @Override
    public int getItemCount() {
        int SHIMMER_ITEM_ITEM=4;

        return showShimmer? SHIMMER_ITEM_ITEM : recentlyViewedList.size();
    }

    public  static class RecentlyViewedViewHolder extends RecyclerView.ViewHolder{

        TextView name,price;
        ImageView image;
        ShimmerFrameLayout shimmerFrameLayout;

        public RecentlyViewedViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.price);
            image=itemView.findViewById(R.id.recnetly_view_prod_image);
            shimmerFrameLayout=itemView.findViewById(R.id.recently_layout_shimmer);


        }
    }

}
