package com.bhaskar.doorstep.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bhaskar.doorstep.ProductDetails;
import com.bhaskar.doorstep.R;
import com.bhaskar.doorstep.model.ProductDTO;
import com.bhaskar.doorstep.model.RecentlyViewed;

import java.util.List;

public class RecentlyViewedAdapter extends RecyclerView.Adapter<RecentlyViewedAdapter.RecentlyViewedViewHolder> {

    Context context;
    List<RecentlyViewed> recentlyViewedList;
    private static final String TAG = "RecentlyViewedAdapter";

    public RecentlyViewedAdapter(Context context, List<RecentlyViewed> recentlyViewedList) {
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

        RecentlyViewed productDTO=recentlyViewedList.get(position);
        Log.d(TAG, "onBindViewHolder: productDTO= "+productDTO.getName());

        holder.name.setText(productDTO.getName());
        holder.description.setText(productDTO.getDescription());
        holder.price.setText(String.valueOf(productDTO.getPrice()));
        holder.qty.setText(productDTO.getQuantity());
        holder.unit.setText(productDTO.getUnit());
        if(position%2==0) {
            holder.bg.setBackgroundResource(R.drawable.card_bg1);
        }
        else {
            holder.bg.setBackgroundResource(R.drawable.card_bg2);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(context, ProductDetails.class);
                i.putExtra("name", recentlyViewedList.get(position).getName());
                i.putExtra("image", productDTO.getBigimageurl());
                i.putExtra("price",recentlyViewedList.get(position).getPrice());
                i.putExtra("desc",recentlyViewedList.get(position).getDescription());
                i.putExtra("qty",recentlyViewedList.get(position).getQuantity());
                i.putExtra("unit",productDTO.getUnit());

                context.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return recentlyViewedList.size();
    }

    public  static class RecentlyViewedViewHolder extends RecyclerView.ViewHolder{

        TextView name, description, price, qty, unit;
        ConstraintLayout bg;

        public RecentlyViewedViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.product_name);
            description = itemView.findViewById(R.id.description);
            price = itemView.findViewById(R.id.price);
            qty = itemView.findViewById(R.id.qty);
            unit = itemView.findViewById(R.id.unit);
            bg = itemView.findViewById(R.id.recently_layout);

        }
    }

}
