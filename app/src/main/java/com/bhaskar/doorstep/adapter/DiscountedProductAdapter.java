package com.bhaskar.doorstep.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.bhaskar.doorstep.model.DiscountedProducts;
import com.bhaskar.doorstep.model.ProductDTO;
import com.bhaskar.doorstep.services.Home;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import static maes.tech.intentanim.CustomIntent.customType;

public class DiscountedProductAdapter extends RecyclerView.Adapter<DiscountedProductAdapter.DiscountedProductViewHolder> {

    Context context;
    List<ProductDTO> discountedProductsList;
    public boolean showShimmer=true;

    public DiscountedProductAdapter(Context context, List<ProductDTO> discountedProductsList) {
        this.context = context;
        this.discountedProductsList = discountedProductsList;
    }

    @NonNull
    @Override
    public DiscountedProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.main_discount_loading_layout, parent, false);
        return new DiscountedProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountedProductViewHolder holder, int position) {
        if (showShimmer)
        {
            holder.shimmerFrameLayout.startShimmer();
        }
        else
        {
            holder.shimmerFrameLayout.stopShimmer();
            holder.shimmerFrameLayout.setShimmer(null);
            holder.discountImageView.setBackground(null);

        Home home=new Home(context);
        ProductDTO productDTO=discountedProductsList.get(position);
        home.loadImageInGlide(holder.discountImageView,productDTO.getImage());
       // holder.title.setText(productDTO.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, ProductDetails.class);
                i.putExtra("cat_name", productDTO.getCategory());
                i.putExtra("selected_product",productDTO);
                i.putExtra("source_to_product_details","Home");
                context.startActivity(i);
                customType(context,"right-to-left");
            }
        });
        }



    }

    @Override
    public int getItemCount() {
        int SHIMMER_ITEM_ITEM=5;

        return showShimmer? SHIMMER_ITEM_ITEM : discountedProductsList.size();
    }

    public static class DiscountedProductViewHolder extends  RecyclerView.ViewHolder{

        ImageView discountImageView;
        ShimmerFrameLayout shimmerFrameLayout;


        public DiscountedProductViewHolder(@NonNull View itemView) {
            super(itemView);


            discountImageView = itemView.findViewById(R.id.discountImage);
            shimmerFrameLayout=itemView.findViewById(R.id.main_discount_shimmer);

           // title=itemView.findViewById(R.id.title_text);
        }
    }
}
