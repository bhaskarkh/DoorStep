package com.bhaskar.doorstep.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bhaskar.doorstep.MainActivity;
import com.bhaskar.doorstep.ProductDetails;
import com.bhaskar.doorstep.R;
import com.bhaskar.doorstep.model.RecentlyViewed;
import com.bumptech.glide.Glide;

import java.util.List;

public class SingleCategoryAdapter extends RecyclerView.Adapter<SingleCategoryAdapter.SingleCategoryViewHolder> {

    Context context;
    List<RecentlyViewed> recentlyViewedList;

    public SingleCategoryAdapter(Context context, List<RecentlyViewed> recentlyViewedList) {
        this.context = context;
        this.recentlyViewedList = recentlyViewedList;
        Log.d("SingleCategoryAdapter","size= "+recentlyViewedList.size());
    }

    @NonNull
    @Override
    public SingleCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_cat_cardview, parent, false);

        return new SingleCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleCategoryViewHolder holder, final int position) {


        holder.description.setText(recentlyViewedList.get(position).getName());
        Glide.with(context).load(recentlyViewedList.get(position).getBigimageurl()).into(holder.category_image);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(context, ProductDetails.class);
                i.putExtra("name", recentlyViewedList.get(position).getName());
                i.putExtra("image", recentlyViewedList.get(position).getBigimageurl());
                i.putExtra("price",recentlyViewedList.get(position).getPrice());
                i.putExtra("desc",recentlyViewedList.get(position).getDescription());
                i.putExtra("qty",recentlyViewedList.get(position).getQuantity());
                i.putExtra("unit",recentlyViewedList.get(position).getUnit());

                context.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return recentlyViewedList.size();
    }


    public  static class SingleCategoryViewHolder extends RecyclerView.ViewHolder{

        TextView description;
        ImageView category_image;


        public SingleCategoryViewHolder(@NonNull View itemView) {
            super(itemView);


            description = itemView.findViewById(R.id.single_cat_text1);
            category_image=itemView.findViewById(R.id.single_cat_imageview);


        }
    }

}
