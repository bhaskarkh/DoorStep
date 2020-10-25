package com.bhaskar.doorstep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bhaskar.doorstep.R;
import com.bhaskar.doorstep.model.ProductDTO;
import com.bhaskar.doorstep.services.ProductServices;
import com.bumptech.glide.Glide;

import java.util.List;

public class AddDiscountProductAdapter extends RecyclerView.Adapter<AddDiscountProductAdapter.ViewHolder> {

    List<ProductDTO> productDTOList;
    Context context;
    ProductServices productServices;

    public AddDiscountProductAdapter( Context context,List<ProductDTO> productDTOList) {
        this.productDTOList = productDTOList;
        this.context = context;
        productServices=new ProductServices(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.add_discount_product_card_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductDTO productDTO=productDTOList.get(position);
        Glide.with(context).load(productDTO.getImage()).into(holder.prodImage);
        holder.prodName.setText(productDTO.getName());
        productServices.addOrRemoveBtnForDiscountProduct(productDTO,holder.addRemoveBtn);

    }

    @Override
    public int getItemCount() {
        return productDTOList.size();
    }

    public  static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView prodImage,addRemoveBtn;
        TextView prodName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            prodImage=itemView.findViewById(R.id.add_discount_prod_image);
            addRemoveBtn=itemView.findViewById(R.id.add_discount_prod_btn);
            prodName=itemView.findViewById(R.id.add_discount_prod_name);

        }
    }
}
