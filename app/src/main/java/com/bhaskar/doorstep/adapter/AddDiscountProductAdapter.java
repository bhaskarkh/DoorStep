package com.bhaskar.doorstep.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bhaskar.doorstep.R;
import com.bhaskar.doorstep.allinterface.ProductInterface;
import com.bhaskar.doorstep.model.ProductDTO;
import com.bhaskar.doorstep.services.Home;
import com.bhaskar.doorstep.services.ProductServices;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AddDiscountProductAdapter extends RecyclerView.Adapter<AddDiscountProductAdapter.ViewHolder>{

    private static final String TAG = "AddDiscountProductAdapt";
    List<ProductDTO> productDTOList;
    Context context;
    ProductServices productServices;
    ProductInterface productInterface;

    public AddDiscountProductAdapter( Context context,List<ProductDTO> productDTOList,ProductServices productServices) {
        this.productDTOList = productDTOList;
        this.context = context;
        this.productServices=productServices;
        this.productInterface=productServices.getProductInterface();




    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.add_discount_product_card_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Home hm=new Home(context);
        productServices.setProductInterface(productInterface);


        ProductDTO productDTO=productDTOList.get(position);
        holder.prodName.setText(productDTO.getName());
        productServices.addOrRemoveBtnForDiscountProduct(productDTO,holder.addRemoveBtn);
       hm.loadImageInGlide(holder.prodImage,productDTO.getImage());
        holder.addRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productServices.addRemoveProductFromDiscount(productDTO);

            }
        });
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
